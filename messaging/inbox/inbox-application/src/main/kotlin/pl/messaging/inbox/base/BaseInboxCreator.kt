package pl.messaging.inbox.base

import pl.messaging.inbox.annotation.InboxListener
import pl.messaging.inbox.exception.InboxMethodShouldContainExactlyOneInboxMessageParameterException
import pl.messaging.inbox.exception.TwoInboxesAreListeningOnTheSameInboxMessageException
import pl.messaging.inbox.model.InboxMessage
import java.lang.reflect.Method
import java.util.function.Consumer

class BaseInboxCreator {
    companion object {

        fun extractBaseInboxes(inboxAwareClasses: List<Any>): List<BaseInbox<InboxMessage>> {
            val result = inboxAwareClasses.stream()
                .distinct()
                .map { inboxAwareBean ->
                    val inboxAwareClass: Class<out Any> = inboxAwareBean::class.java
                    inboxAwareClass.declaredMethods.toList()
                        .stream()
                        .filter { method -> method.isAnnotationPresent(InboxListener::class.java) }
                        .map { methodInbox -> toBaseInbox(methodInbox, inboxAwareBean) }
                }
                .flatMap { it }
                .toList()

            result.forEach { checked ->
                result.forEach { checkIfIsDuplicated(checked, it) }
            }

            return result
        }

        private fun toBaseInbox(methodInbox: Method, clazz: Any): BaseInbox<InboxMessage> {
            val inboxAnnotation: InboxListener = methodInbox.annotations
                .filterIsInstance<InboxListener>()
                .first()

            val classOfInboxMethod = obtainClassOfInboxMethod(methodInbox)
            val consumer: Consumer<InboxMessage> = Consumer { param: Any ->
                methodInbox.invoke(clazz, param)
            }

            return BaseInbox(
                inboxAnnotation.retryCount,
                inboxAnnotation.batchSize,
                inboxAnnotation.cron,
                classOfInboxMethod,
                consumer
            )
        }

        private fun checkIfIsDuplicated(
            baseInbox1: BaseInbox<out InboxMessage>,
            baseInbox2: BaseInbox<out InboxMessage>
        ) {
            if (baseInbox1 != baseInbox2 && baseInbox1.type == baseInbox2.type) {
                throw TwoInboxesAreListeningOnTheSameInboxMessageException(baseInbox1.type)
            }
        }

        private fun obtainClassOfInboxMethod(inboxMethod: Method): Class<out InboxMessage> {
            if (inboxMethod.parameters.size != 1) {
                throw InboxMethodShouldContainExactlyOneInboxMessageParameterException()
            }

            return inboxMethod.parameterTypes[0] as Class<out InboxMessage>
        }

    }
}