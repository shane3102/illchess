package pl.messaging.inbox

import org.springframework.beans.factory.annotation.Autowired
import pl.messaging.inbox.aggregator.Inbox
import pl.messaging.inbox.model.InboxMessage
import pl.messaging.inbox.model.TestInboxMessage1
import pl.messaging.inbox.service.TestInboxMessageService

class InboxTest extends SpecificationIT {

    @Autowired
    Inbox inbox

    @Autowired
    TestInboxMessageService testInboxMessageService

    def 'inbox simple test'() {
        given:

        when:
        sentMessages.forEach { inbox.saveMessage(it) }
        Thread.sleep(3000)

        then:
        testInboxMessageService.executeCountByInbox.entrySet().every { countByMessage ->
            expectedCountByMessage.get(countByMessage.key) == countByMessage.value
        }

        where:
        sentMessages                                                                                               | expectedCountByMessage
        [new TestInboxMessage1(UUID.randomUUID()), new TestInboxMessage1(UUID.randomUUID())] as List<InboxMessage> | Map.of(TestInboxMessage1.class, 2)

    }
}
