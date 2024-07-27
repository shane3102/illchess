package pl.messaging.inbox.service

import org.springframework.stereotype.Component
import pl.messaging.inbox.annotation.InboxAwareComponent
import pl.messaging.inbox.annotation.InboxListener
import pl.messaging.inbox.model.*

@Component
@InboxAwareComponent
class TestInboxMessageService {
    static final def DEFAULT_EXECUTE_COUNT_BY_INBOX = new HashMap<>(
            Map.of(
                    TestInboxMessageFixedRate.class as Class<InboxMessage>, 0,
                    TestInboxMessageFixedDelay.class as Class<InboxMessage>, 0,
                    TestInboxMessageFailing.class as Class<InboxMessage>, 0,
                    TestInboxMessageCron.class as Class<InboxMessage>, 0
            )
    )

    Map<Class<InboxMessage>, Integer> executeCountByInbox = DEFAULT_EXECUTE_COUNT_BY_INBOX

    def resetCount() {
        executeCountByInbox = DEFAULT_EXECUTE_COUNT_BY_INBOX
    }

    @InboxListener(
            type = TestInboxMessageFixedRate,
            retryCount = 2,
            batchSize = 10,
            fixedRate = 100
    )
    void testInboxMessageFixedRate(TestInboxMessageFixedRate message) {
        def count = executeCountByInbox.get(message.class)
        count = count + 1
        executeCountByInbox.put(message.class, count)
    }

    @InboxListener(
            type = TestInboxMessageFixedDelay,
            retryCount = 2,
            batchSize = 10,
            fixedDelay = 100
    )
    void testInboxMessageFixedDelay(TestInboxMessageFixedDelay message) {
        def count = executeCountByInbox.get(message.class)
        count = count + 1
        executeCountByInbox.put(message.class, count)
    }

    @InboxListener(
            type = TestInboxMessageCron,
            retryCount = 2,
            batchSize = 10,
            cron = "* * * * * *"
    )
    void testInboxMessageCron(TestInboxMessageCron message) {
        def count = executeCountByInbox.get(message.class)
        count = count + 1
        executeCountByInbox.put(message.class, count)
    }

    @InboxListener(
            type = TestInboxMessageFailing,
            retryCount = 3,
            batchSize = 10,
            fixedRate = 100
    )
    void testInboxMessageFailing(TestInboxMessageFailing message) {
        throw new RuntimeException()
    }
}
