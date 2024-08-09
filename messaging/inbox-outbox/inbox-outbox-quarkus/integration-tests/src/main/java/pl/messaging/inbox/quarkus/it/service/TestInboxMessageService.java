package pl.messaging.inbox.quarkus.it.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import pl.messaging.annotation.InboxOutboxListener;
import pl.messaging.inbox.quarkus.it.model.TestMessage1;
import pl.messaging.inbox.quarkus.it.model.TestMessage2;
import pl.messaging.inbox.quarkus.it.model.TestMessage3;
import pl.messaging.inbox.quarkus.it.model.TestMessageFailing;
import pl.messaging.model.Message;
import pl.messaging.quarkus.runtime.annotation.MessagingAwareComponent;

@ApplicationScoped
@MessagingAwareComponent
public class TestInboxMessageService {

    static final Map<Class<? extends Message>, Integer> DEFAULT_EXECUTE_COUNT_BY_INBOX = new HashMap<>(
        Map.of(
            TestMessage3.class, 0,
            TestMessage2.class, 0,
            TestMessageFailing.class, 0,
            TestMessage1.class, 0
        )
    );

    public Map<Class<? extends Message>, Integer> executeCountByInbox = DEFAULT_EXECUTE_COUNT_BY_INBOX;

    public void resetCount() {
        executeCountByInbox = DEFAULT_EXECUTE_COUNT_BY_INBOX;
    }

    @InboxOutboxListener(
        retryCount = 2,
        batchSize = 10,
        cron = "* * * * * ?"
    )
    public void testInboxMessageFixedRate(TestMessage3 message) {
        Integer count = executeCountByInbox.get(message.getClass());
        count = count + 1;
        executeCountByInbox.put(message.getClass(), count);
    }

    @InboxOutboxListener(
        retryCount = 2,
        batchSize = 10,
        cron = "* * * * * ?"
    )
    public void testInboxMessageFixedDelay(TestMessage2 message) {
        Integer count = executeCountByInbox.get(message.getClass());
        count = count + 1;
        executeCountByInbox.put(message.getClass(), count);
    }

    @InboxOutboxListener(
        retryCount = 2,
        batchSize = 10,
        cron = "* * * * * ?"
    )
    public void testInboxMessageCron(TestMessage1 message) {
        Integer count = executeCountByInbox.get(message.getClass());
        count = count + 1;
        executeCountByInbox.put(message.getClass(), count);
    }

    @InboxOutboxListener(
        retryCount = 3,
        batchSize = 10,
        cron = "* * * * * ?"
    )
    public void testInboxMessageFailing(TestMessageFailing message) {
        throw new RuntimeException();
    }

}
