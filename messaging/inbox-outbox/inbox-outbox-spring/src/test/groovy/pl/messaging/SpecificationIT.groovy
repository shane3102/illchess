package pl.messaging

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.spock.Testcontainers
import pl.messaging.aggregator.InboxOutbox
import pl.messaging.configuration.InboxOutboxConfiguration
import pl.messaging.service.TestInboxMessageService
import spock.lang.Specification

@SpringBootTest(classes = [InboxOutbox, InboxOutboxConfiguration, TestInboxMessageService])
@Testcontainers
@EnableScheduling
@ContextConfiguration
@ExtendWith(SpringExtension.class)
abstract class SpecificationIT extends Specification {

}