package pl.messaging.inbox

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.spock.Testcontainers
import pl.messaging.inbox.aggregator.Inbox
import pl.messaging.inbox.configuration.InboxConfiguration
import spock.lang.Specification

@SpringBootTest(classes = [Inbox, InboxConfiguration] )
@Testcontainers
@EnableScheduling
@ContextConfiguration
@ExtendWith(SpringExtension.class)
abstract class SpecificationIT extends Specification {

}