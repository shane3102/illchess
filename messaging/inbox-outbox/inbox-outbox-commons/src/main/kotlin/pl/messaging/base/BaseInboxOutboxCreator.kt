package pl.messaging.base

import pl.messaging.annotation.InboxOutboxListener
import pl.messaging.exception.InboxOutboxMethodShouldContainExactlyOneInboxOutboxMessageParameterException
import pl.messaging.exception.TwoInboxOutboxAreListeningOnTheSameMessageException
import pl.messaging.model.Message
import java.lang.reflect.Method
import java.util.function.Consumer

class BaseInboxOutboxCreator {
    companion object {

        fun extractBaseInboxes(inboxAwareClasses: List<Any>): List<BaseInboxOutbox<Message>> {
            val result = inboxAwareClasses.stream()
                .distinct()
                .map { inboxAwareBean ->
                    val inboxAwareClass: Class<out Any> = inboxAwareBean::class.java
                    inboxAwareClass.declaredMethods.toList()
                        .stream()
                        .filter { method -> method.isAnnotationPresent(InboxOutboxListener::class.java) }
                        .map { methodInbox -> toBaseInbox(methodInbox, inboxAwareBean) }
                }
                .flatMap { it }
                .toList()

            result.forEach { checked ->
                result.forEach { checkIfIsDuplicated(checked, it) }
            }

            return result
        }

        private fun toBaseInbox(methodInbox: Method, clazz: Any): BaseInboxOutbox<Message> {
            val inboxAnnotation: InboxOutboxListener = methodInbox.annotations
                .filterIsInstance<InboxOutboxListener>()
                .first()

            val classOfInboxMethod = obtainClassOfInboxMethod(methodInbox)
            val consumer: Consumer<Message> = Consumer { param: Any ->
                methodInbox.invoke(clazz, param)
            }

            return BaseInboxOutbox(
                inboxAnnotation.retryCount,
                inboxAnnotation.batchSize,
                inboxAnnotation.cron,
                classOfInboxMethod,
                consumer
            )
        }

        private fun checkIfIsDuplicated(
            baseInboxOutbox1: BaseInboxOutbox<out Message>,
            baseInboxOutbox2: BaseInboxOutbox<out Message>
        ) {
            if (baseInboxOutbox1 != baseInboxOutbox2 && baseInboxOutbox1.type == baseInboxOutbox2.type) {
                throw TwoInboxOutboxAreListeningOnTheSameMessageException(baseInboxOutbox1.type)
            }
        }

        private fun obtainClassOfInboxMethod(inboxMethod: Method): Class<out Message> {
            if (inboxMethod.parameters.size != 1) {
                throw InboxOutboxMethodShouldContainExactlyOneInboxOutboxMessageParameterException()
            }

            return inboxMethod.parameterTypes[0] as Class<out Message>
        }

    }
}