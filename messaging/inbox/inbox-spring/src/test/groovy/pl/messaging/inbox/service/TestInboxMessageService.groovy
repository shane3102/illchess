package pl.messaging.inbox.service


import org.springframework.stereotype.Component
import pl.messaging.inbox.annotation.InboxAwareComponent
import pl.messaging.inbox.annotation.InboxListener
import pl.messaging.inbox.model.InboxMessage
import pl.messaging.inbox.model.TestInboxMessage1

import static pl.messaging.inbox.annotation.configuration.InboxWorkingMode.SEND_DIRECTLY_TO_INBOX

@Component
@InboxAwareComponent
class TestInboxMessageService {

    Map<Class<InboxMessage>, Integer> executeCountByInbox = new HashMap<>(
            Map.of(
                    TestInboxMessage1.class as Class<InboxMessage>, 0
            )
    )

    @InboxListener(
            workingMode = SEND_DIRECTLY_TO_INBOX,
            type = TestInboxMessage1,
            retryCount = 2,
            batchSize = 10,
            cron = "* * * * * *"
    )
    void testInboxMessage1(TestInboxMessage1 message1) {
        def count = executeCountByInbox.get(message1.class)
        count = count + 1
        executeCountByInbox.put(message1.class, count)
    }
}
