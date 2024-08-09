package pl.messaging.inbox.quarkus.it;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import pl.messaging.inbox.quarkus.it.model.TestMessage1;
import pl.messaging.inbox.quarkus.it.model.TestMessage2;
import pl.messaging.inbox.quarkus.it.model.TestMessage3;
import pl.messaging.inbox.quarkus.it.model.TestMessageFailing;
import pl.messaging.inbox.quarkus.it.service.TestInboxMessageService;
import pl.messaging.model.Message;
import pl.messaging.quarkus.runtime.aggregator.InboxOutbox;
import pl.messaging.quarkus.runtime.annotation.MessagingAwareComponent;
import pl.messaging.repository.LoadMessages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class InboxTest {

    @Inject
    InboxOutbox inboxOutbox;

    @Inject
    @MessagingAwareComponent
    TestInboxMessageService testInboxMessageService;

    @Inject
    LoadMessages loadMessages;

    @Test
    public void inboxSimpleTest() {
        // given
        List<Message> sentMessages = List.of(new TestMessage3(), new TestMessage3(), new TestMessage2(), new TestMessageFailing(), new TestMessage1());
        Map<Class<? extends Message>, Integer> expectedCountByMessage = Map.of(TestMessage3.class, 2, TestMessage2.class, 1, TestMessageFailing.class, 0, TestMessage1.class, 1);

        // when
        sentMessages.forEach(inboxOutbox::saveMessage);
        try {
            Thread.sleep(1100);
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

        inboxOutbox.saveMessage(new TestMessageFailing(id));
        try {
            Thread.sleep(1500);
        } catch (Exception ignored) {
        }

        List<Message> failingMessages = loadMessages.loadLatestByTypeNonExpired(TestMessageFailing.class.toString(), 5, 5);
        Message sentInboxMessage = failingMessages.stream().filter(failingInboxMessage -> Objects.equals(failingInboxMessage.getId(), id)).findFirst().orElseThrow(AssertionError::new);

        assertTrue(sentInboxMessage.getRetryCount() >= 1, "Expected to have at least one in retry count");
        assertTrue(sentInboxMessage.getRetryCount() <= 3, "Expected to have at most 3 in retry count");

    }

    @Test
    public void shouldHaveExpectedCountOfRetryWithMaxValueOnFailingInboxMessage() {
        testInboxMessageService.resetCount();
        UUID id = UUID.randomUUID();

        inboxOutbox.saveMessage(new TestMessageFailing(id));
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {
        }

        List<Message> failingMessages = loadMessages.loadLatestByTypeNonExpired(TestMessageFailing.class.toString(), 5, 5);
        Message sentMessage = failingMessages.stream().filter(failingInboxMessage -> Objects.equals(failingInboxMessage.getId(), id)).findFirst().orElseThrow(AssertionError::new);

        assertEquals(3, sentMessage.getRetryCount(), "Expected to have exactly 3 in retry count");

    }

}
