package pl.messaging.inbox.quarkus.deployment

import io.quarkus.arc.deployment.AdditionalBeanBuildItem
import io.quarkus.deployment.annotations.BuildStep
import io.quarkus.deployment.builditem.FeatureBuildItem
import pl.messaging.inbox.quarkus.runtime.aggregator.Inbox
import pl.messaging.inbox.quarkus.runtime.annotation.InboxAwareComponent
import pl.messaging.inbox.quarkus.runtime.configuration.InboxConfiguration

class InboxQuarkusProcessor {

    private val FEATURE = "inbox-quarkus"

    @BuildStep
    fun feature(): FeatureBuildItem {
        return FeatureBuildItem(FEATURE)
    }

    @BuildStep
    fun inbox(): AdditionalBeanBuildItem {
        return AdditionalBeanBuildItem(
            Inbox::class.java,
            InboxConfiguration::class.java,
            InboxAwareComponent::class.java
        )
    }


}