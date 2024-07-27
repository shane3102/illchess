package pl.messaging.inbox

import org.springframework.beans.factory.annotation.Autowired
import pl.messaging.inbox.aggregator.Inbox
import pl.messaging.inbox.model.InboxMessage
import pl.messaging.inbox.model.TestInboxMessage1
import pl.messaging.inbox.model.TestInboxMessage2
import pl.messaging.inbox.model.TestInboxMessageFailing
import pl.messaging.inbox.repository.LoadInboxMessages
import pl.messaging.inbox.service.TestInboxMessageService

class InboxTest extends SpecificationIT {

    @Autowired
    Inbox inbox

    @Autowired
    TestInboxMessageService testInboxMessageService

    @Autowired
    LoadInboxMessages loadInboxMessages

    def 'inbox simple test'() {
        given:
        testInboxMessageService.resetCount()

        when:
        sentMessages.forEach { inbox.saveMessage(it) }
        Thread.sleep(3000)

        then:
        testInboxMessageService.executeCountByInbox.entrySet().every { countByMessage ->
            expectedCountByMessage.get(countByMessage.key) == countByMessage.value
        }

        where:
        sentMessages                                                                                                                     | expectedCountByMessage
        [new TestInboxMessage1(), new TestInboxMessage1(), new TestInboxMessage2(), new TestInboxMessageFailing()] as List<InboxMessage> | Map.of(TestInboxMessage1.class, 2, TestInboxMessage2.class, 1, TestInboxMessageFailing.class, 0)

    }

    def 'should have expected count of retry on failing inbox message'() {
        given:
        testInboxMessageService.resetCount()
        def id = UUID.randomUUID()

        when:
        inbox.saveMessage(new TestInboxMessageFailing(id))
        Thread.sleep(3000)

        then:
        def failingInboxMessages = loadInboxMessages.loadLatestByTypeNonExpired(TestInboxMessageFailing.class.toString(), 5, 5)
        with(failingInboxMessages) {
            with(it.find { it.id == id }) {
                it.retryCount == 3
            }
        }

    }

    def 'should have expected count of retry with max value on failing inbox message'() {
        given:
        testInboxMessageService.resetCount()
        def id = UUID.randomUUID()

        when:
        inbox.saveMessage(new TestInboxMessageFailing(id))
        Thread.sleep(5000)

        then:
        def failingInboxMessages = loadInboxMessages.loadLatestByTypeNonExpired(TestInboxMessageFailing.class.toString(), 5, 5)
        with(failingInboxMessages) {
            with(it.find { it.id == id }) {
                it.retryCount == 3
            }
        }

    }
}
