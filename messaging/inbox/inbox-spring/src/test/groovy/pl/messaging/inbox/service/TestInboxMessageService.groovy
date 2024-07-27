package pl.messaging.inbox.service

import org.springframework.stereotype.Component
import pl.messaging.inbox.annotation.InboxAwareComponent
import pl.messaging.inbox.annotation.InboxListener
import pl.messaging.inbox.model.InboxMessage
import pl.messaging.inbox.model.TestInboxMessage1
import pl.messaging.inbox.model.TestInboxMessage2
import pl.messaging.inbox.model.TestInboxMessageFailing

@Component
@InboxAwareComponent
class TestInboxMessageService {
    static  final def DEFAULT_EXECUTE_COUNT_BY_INBOX = new HashMap<>(
            Map.of(
                    TestInboxMessage1.class as Class<InboxMessage>, 0,
                    TestInboxMessage2.class as Class<InboxMessage>, 0,
                    TestInboxMessageFailing.class as Class<InboxMessage>, 0
            )
    )

    Map<Class<InboxMessage>, Integer> executeCountByInbox = DEFAULT_EXECUTE_COUNT_BY_INBOX

    def resetCount() {
        executeCountByInbox = DEFAULT_EXECUTE_COUNT_BY_INBOX
    }

    @InboxListener(
            type = TestInboxMessage1,
            retryCount = 2,
            batchSize = 10,
            cron = "* * * * * *"
    )
    void testInboxMessage1(TestInboxMessage1 message) {
        def count = executeCountByInbox.get(message.class)
        count = count + 1
        executeCountByInbox.put(message.class, count)
    }

    @InboxListener(
            type = TestInboxMessage2,
            retryCount = 2,
            batchSize = 10,
            cron = "* * * * * *"
    )
    void testInboxMessage2(TestInboxMessage2 message) {
        def count = executeCountByInbox.get(message.class)
        count = count + 1
        executeCountByInbox.put(message.class, count)
    }

    @InboxListener(
            type = TestInboxMessageFailing,
            retryCount = 3,
            batchSize = 10,
            cron = "* * * * * *"
    )
    void testInboxMessageFailing(TestInboxMessageFailing message) {
        throw new RuntimeException()
    }
}
