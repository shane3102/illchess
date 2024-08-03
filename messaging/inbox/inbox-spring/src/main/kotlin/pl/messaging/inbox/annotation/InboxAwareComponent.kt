package pl.messaging.inbox.annotation

import org.springframework.beans.factory.annotation.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class InboxAwareComponent
