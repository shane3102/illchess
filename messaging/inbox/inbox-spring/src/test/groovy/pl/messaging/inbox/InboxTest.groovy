package pl.messaging.inbox

import org.springframework.beans.factory.annotation.Autowired
import pl.messaging.inbox.aggregator.Inbox
import pl.messaging.inbox.model.*
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
        Thread.sleep(1500)

        then:
        testInboxMessageService.executeCountByInbox.entrySet().every { countByMessage ->
            expectedCountByMessage.get(countByMessage.key) == countByMessage.value
        }

        where:
        sentMessages                                                                                                                                                                          | _
        [new TestInboxMessageFixedRate(), new TestInboxMessageFixedRate(), new TestInboxMessageFixedDelay(), new TestInboxMessageFailing(), new TestInboxMessageCron()] as List<InboxMessage> | _
        __
        expectedCountByMessage                                                                                                                     | _
        Map.of(TestInboxMessageFixedRate.class, 2, TestInboxMessageFixedDelay.class, 1, TestInboxMessageFailing.class, 0, TestInboxMessageCron, 1) | _

    }

    def 'should have expected count of retry on failing inbox message'() {
        given:
        testInboxMessageService.resetCount()
        def id = UUID.randomUUID()

        when:
        inbox.saveMessage(new TestInboxMessageFailing(id))
        Thread.sleep(200)

        then:
        def failingInboxMessages = loadInboxMessages.loadLatestByTypeNonExpired(TestInboxMessageFailing.class.toString(), 5, 5)
        with(failingInboxMessages) {
            with(it.find { it.id == id }) {
                it.retryCount <= 3
                it.retryCount > 1
            }
        }

    }

    def 'should have expected count of retry with max value on failing inbox message'() {
        given:
        testInboxMessageService.resetCount()
        def id = UUID.randomUUID()

        when:
        inbox.saveMessage(new TestInboxMessageFailing(id))
        Thread.sleep(500)

        then:
        def failingInboxMessages = loadInboxMessages.loadLatestByTypeNonExpired(TestInboxMessageFailing.class.toString(), 5, 5)
        with(failingInboxMessages) {
            with(it.find { it.id == id }) {
                it.retryCount == 3
            }
        }

    }
}
