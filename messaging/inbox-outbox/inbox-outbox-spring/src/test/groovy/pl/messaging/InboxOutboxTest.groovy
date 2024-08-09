package pl.messaging


import org.springframework.beans.factory.annotation.Autowired
import pl.messaging.aggregator.InboxOutbox
import pl.messaging.model.*
import pl.messaging.repository.LoadMessages
import pl.messaging.service.TestInboxMessageService

class InboxOutboxTest extends SpecificationIT {

    @Autowired
    InboxOutbox inbox

    @Autowired
    TestInboxMessageService testInboxMessageService

    @Autowired
    LoadMessages loadInboxMessages

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
        sentMessages                                                                                                                | _
        [new TestMessage1(), new TestMessage1(), new TestMessage2(), new TestMessageFailing(), new TestMessage3()] as List<Message> | _
        __
        expectedCountByMessage                                                                             | _
        Map.of(TestMessage1.class, 2, TestMessage2.class, 1, TestMessageFailing.class, 0, TestMessage3, 1) | _

    }

    def 'should have expected count of retry on failing inbox message'() {
        given:
        testInboxMessageService.resetCount()
        def id = UUID.randomUUID()

        when:
        inbox.saveMessage(new TestMessageFailing(id))
        Thread.sleep(2000)

        then:
        def failingInboxMessages = loadInboxMessages.loadLatestByTypeNonExpired(TestMessageFailing.class.toString(), 5, 5)
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
        inbox.saveMessage(new TestMessageFailing(id))
        Thread.sleep(5000)

        then:
        def failingInboxMessages = loadInboxMessages.loadLatestByTypeNonExpired(TestMessageFailing.class.toString(), 5, 5)
        with(failingInboxMessages) {
            with(it.find { it.id == id }) {
                it.retryCount == 3
            }
        }

    }
}
