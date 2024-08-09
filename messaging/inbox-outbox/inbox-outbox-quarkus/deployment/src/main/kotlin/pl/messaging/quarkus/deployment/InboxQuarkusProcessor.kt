package pl.messaging.quarkus.deployment

import io.quarkus.arc.deployment.AdditionalBeanBuildItem
import io.quarkus.deployment.annotations.BuildStep
import io.quarkus.deployment.builditem.FeatureBuildItem
import pl.messaging.quarkus.runtime.aggregator.InboxOutbox
import pl.messaging.quarkus.runtime.annotation.MessagingAwareComponent
import pl.messaging.quarkus.runtime.configuration.InboxOutboxConfiguration

class InboxQuarkusProcessor {

    private val FEATURE = "inbox-quarkus"

    @BuildStep
    fun feature(): FeatureBuildItem {
        return FeatureBuildItem(FEATURE)
    }

    @BuildStep
    fun inbox(): AdditionalBeanBuildItem {
        return AdditionalBeanBuildItem(
            InboxOutbox::class.java,
            InboxOutboxConfiguration::class.java,
            MessagingAwareComponent::class.java
        )
    }


}