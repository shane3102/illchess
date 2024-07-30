package pl.messaging.inbox.base

import pl.messaging.inbox.annotation.InboxListener
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

            val consumer: Consumer<InboxMessage> = Consumer { param: Any ->
                methodInbox.invoke(clazz, param)
            }

            return BaseInbox(
                inboxAnnotation.retryCount,
                inboxAnnotation.batchSize,
                if (inboxAnnotation.cron == "") null else inboxAnnotation.cron,
                if (inboxAnnotation.fixedDelay == -1) null else inboxAnnotation.fixedDelay,
                if (inboxAnnotation.fixedRate == -1) null else inboxAnnotation.fixedRate,
                if (inboxAnnotation.initialDelay == -1) null else inboxAnnotation.initialDelay,
                inboxAnnotation.type.java,
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

    }
}