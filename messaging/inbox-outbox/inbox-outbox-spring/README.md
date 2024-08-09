# Inbox-Outbox - Spring

Biblioteka wspierająca asynchroniczne odbieranie komunikatów pomiędzy serwisami z wykorzystaniem wzorca inbox/outbox dla aplikacji opartych na Springu.

### Konfiguracja
<details>
    <summary>Dependency - konfiguracja maven</summary>

```
<dependency>
    <groupId>pl.illchess.messaging</groupId>
    <artifactId>inbox-outbox-spring</artifactId>
    <version>${inbox-outbox.version}</version>
</dependency>
```
</details>

<details>
    <summary>Konfiguracja beanów Spring</summary>
    
Zalecana jest implementacja interfejsów `LoadMessages`, `SaveMessage` oraz `DeleteMessage`. 
Domyślne implementacje działają na podstawie repozytorium in-memory. 
    

</details>

<details>
  <summary>Konfiguracja Aplikacji</summary>

Należy dodać adnotacje do naszej klasy odpowiadającej za stworzenie aplikacji SpringBoot

```java
@EnableScheduling
```

</details>

### Przykład użycia

#### Obsługa eventów z inboxa/outboxa:

Wysyłanie eventów na inboxa/outboxa:
```java
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SomeEventHandlerService {

    private final InboxOutbox inboxOutbox;

    @EventListener(SomeEvent.class)
    public void handleEvent(SomeEvent event) {
        SomeInboxOutboxEvent inboxOutboxEvent = event.toInboxOutboxEvent();
        inboxOutbox.publish(inboxOutboxEvent);
    }
}

```

Obsługa eventu wysłanego na inboxa/outboxa:

```java
import pl.messaging.inbox.annotation.MessagingAwareComponent;

@Service
@MessagingAwareComponent
public class SomeHandlerService {

    @InboxOutboxListener(
            retryCount = 2,
            batchSize = 10,
            cron = "* * * * * *"
    )
    public void handleSomeInboxEvent(SomeInboxOutboxEvent someEvent) {
        // implementacja - wysłanie eventu dalej lub obsłużenie eventu
    }
}
```
