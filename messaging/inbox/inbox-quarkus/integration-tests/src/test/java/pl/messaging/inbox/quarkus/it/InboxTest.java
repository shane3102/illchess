package pl.messaging.inbox.quarkus.it;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import pl.messaging.inbox.model.InboxMessage;
import pl.messaging.inbox.quarkus.it.model.TestInboxMessageCron;
import pl.messaging.inbox.quarkus.it.model.TestInboxMessageFailing;
import pl.messaging.inbox.quarkus.it.model.TestInboxMessageFixedDelay;
import pl.messaging.inbox.quarkus.it.model.TestInboxMessageFixedRate;
import pl.messaging.inbox.quarkus.it.service.TestInboxMessageService;
import pl.messaging.inbox.quarkus.runtime.aggregator.Inbox;
import pl.messaging.inbox.quarkus.runtime.annotation.InboxAwareComponent;
import pl.messaging.inbox.repository.LoadInboxMessages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class InboxTest {

    @Inject
    Inbox inbox;

    @Inject
    @InboxAwareComponent
    TestInboxMessageService testInboxMessageService;

    @Inject
    LoadInboxMessages loadInboxMessages;

    @Test
    public void inboxSimpleTest() {
        // given
        List<InboxMessage> sentMessages = List.of(new TestInboxMessageFixedRate(), new TestInboxMessageFixedRate(), new TestInboxMessageFixedDelay(), new TestInboxMessageFailing(), new TestInboxMessageCron());
        Map<Class<? extends InboxMessage>, Integer> expectedCountByMessage = Map.of(TestInboxMessageFixedRate.class, 2, TestInboxMessageFixedDelay.class, 1, TestInboxMessageFailing.class, 0, TestInboxMessageCron.class, 1);

        // when
        sentMessages.forEach(inbox::saveMessage);
        try {
            Thread.sleep(1500);
        } catch (Exception ignored) {
        }

        // then

        expectedCountByMessage.forEach((inboxMessageType, expectedCount) -> {
            Integer realCount = testInboxMessageService.executeCountByInbox.get(inboxMessageType);
            assertEquals(expectedCount, realCount, "Count for class %s not equal expected. Real count: %s, Expected count: %s".formatted(inboxMessageType, realCount, expectedCount));
        });

    }

    @Test
    public void shouldHaveExpectedCountOfRetryOnFailingInboxMessage() {
        testInboxMessageService.resetCount();
        UUID id = UUID.randomUUID();

        inbox.saveMessage(new TestInboxMessageFailing(id));
        try {
            Thread.sleep(1500);
        } catch (Exception ignored) {
        }

        List<InboxMessage> failingInboxMessages = loadInboxMessages.loadLatestByTypeNonExpired(TestInboxMessageFailing.class.toString(), 5, 5);
        InboxMessage sentInboxMessage = failingInboxMessages.stream().filter(failingInboxMessage -> Objects.equals(failingInboxMessage.getId(), id)).findFirst().orElseThrow(AssertionError::new);

        assertTrue(sentInboxMessage.getRetryCount() >= 1, "Expected to have at least one in retry count");
        assertTrue(sentInboxMessage.getRetryCount() <= 3, "Expected to have at most 3 in retry count");

    }

    @Test
    public void shouldHaveExpectedCountOfRetryWithMaxValueOnFailingInboxMessage() {
        testInboxMessageService.resetCount();
        UUID id = UUID.randomUUID();

        inbox.saveMessage(new TestInboxMessageFailing(id));
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {
        }

        List<InboxMessage> failingInboxMessages = loadInboxMessages.loadLatestByTypeNonExpired(TestInboxMessageFailing.class.toString(), 5, 5);
        InboxMessage sentInboxMessage = failingInboxMessages.stream().filter(failingInboxMessage -> Objects.equals(failingInboxMessage.getId(), id)).findFirst().orElseThrow(AssertionError::new);

        assertEquals(3, sentInboxMessage.getRetryCount(), "Expected to have exactly 3 in retry count");

    }

}
