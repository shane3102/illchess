package pl.messaging.inbox.quarkus.runtime.annotation

import jakarta.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class InboxAwareComponent
