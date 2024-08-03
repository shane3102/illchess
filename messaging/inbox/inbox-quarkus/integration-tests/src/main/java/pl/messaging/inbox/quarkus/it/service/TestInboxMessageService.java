package pl.messaging.inbox.quarkus.it.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;

import pl.messaging.inbox.annotation.InboxListener;
import pl.messaging.inbox.model.InboxMessage;
import pl.messaging.inbox.quarkus.it.model.TestInboxMessageCron;
import pl.messaging.inbox.quarkus.it.model.TestInboxMessageFailing;
import pl.messaging.inbox.quarkus.it.model.TestInboxMessageFixedDelay;
import pl.messaging.inbox.quarkus.it.model.TestInboxMessageFixedRate;
import pl.messaging.inbox.quarkus.runtime.annotation.InboxAwareComponent;

@ApplicationScoped
@InboxAwareComponent
public class TestInboxMessageService {

    static final Map<Class<? extends InboxMessage>, Integer> DEFAULT_EXECUTE_COUNT_BY_INBOX = new HashMap<>(
        Map.of(
            TestInboxMessageFixedRate.class, 0,
            TestInboxMessageFixedDelay.class, 0,
            TestInboxMessageFailing.class, 0,
            TestInboxMessageCron.class, 0
        )
    );

    public Map<Class<? extends InboxMessage>, Integer> executeCountByInbox = DEFAULT_EXECUTE_COUNT_BY_INBOX;

    public void resetCount() {
        executeCountByInbox = DEFAULT_EXECUTE_COUNT_BY_INBOX;
    }

    @InboxListener(
        retryCount = 2,
        batchSize = 10,
        cron = "* * * * * ?"
    )
    public void testInboxMessageFixedRate(TestInboxMessageFixedRate message) {
        Integer count = executeCountByInbox.get(message.getClass());
        count = count + 1;
        executeCountByInbox.put(message.getClass(), count);
    }

    @InboxListener(
        retryCount = 2,
        batchSize = 10,
        cron = "* * * * * ?"
    )
    public void testInboxMessageFixedDelay(TestInboxMessageFixedDelay message) {
        Integer count = executeCountByInbox.get(message.getClass());
        count = count + 1;
        executeCountByInbox.put(message.getClass(), count);
    }

    @InboxListener(
        retryCount = 2,
        batchSize = 10,
        cron = "* * * * * ?"
    )
    public void testInboxMessageCron(TestInboxMessageCron message) {
        Integer count = executeCountByInbox.get(message.getClass());
        count = count + 1;
        executeCountByInbox.put(message.getClass(), count);
    }

    @InboxListener(
        retryCount = 3,
        batchSize = 10,
        cron = "* * * * * ?"
    )
    public void testInboxMessageFailing(TestInboxMessageFailing message) {
        throw new RuntimeException();
    }

}
