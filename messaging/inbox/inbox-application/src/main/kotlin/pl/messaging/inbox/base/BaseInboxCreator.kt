package pl.messaging.inbox.base

import pl.messaging.inbox.annotation.InboxListener
import pl.messaging.inbox.model.InboxMessage
import java.lang.reflect.Method
import java.util.function.Consumer

class BaseInboxCreator {
    companion object {

        fun extractBaseInboxes(inboxAwareClasses: List<Class<out Any>>): List<BaseInbox<InboxMessage>> {
            return inboxAwareClasses.stream()
                .map { inboxAwareClass ->
                    inboxAwareClass.declaredMethods.toList()
                        .stream()
                        .filter { method -> method.isAnnotationPresent(InboxListener::class.java) }
                        .map { methodInbox -> toBaseInbox(methodInbox, inboxAwareClass) }
                }
                .flatMap { it }
                .toList()
        }

        private fun toBaseInbox(methodInbox: Method, clazz: Class<out Any>): BaseInbox<InboxMessage> {
            val inboxAnnotation: InboxListener = methodInbox.annotations
                .filterIsInstance<InboxListener>()
                .first()

            val consumer: Consumer<InboxMessage> = Consumer { param: Any ->
                methodInbox.invoke(clazz, param)
            }

            return BaseInbox(
                inboxAnnotation.workingMode,
                inboxAnnotation.retryCount,
                inboxAnnotation.batchSize,
                inboxAnnotation.cron,
                inboxAnnotation.type.java,
                consumer
            )
        }

    }
}