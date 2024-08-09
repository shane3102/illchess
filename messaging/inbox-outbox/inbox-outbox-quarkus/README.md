# Inbox-Outbox - Quarkus

Biblioteka wspierająca asynchroniczne odbieranie komunikatów pomiędzy serwisami z wykorzystaniem wzorca inbox/outbox dla aplikacji opartych na Quarkusie.

### Konfiguracja
<details>
    <summary>Dependency - konfiguracja maven</summary>

```
<dependency>
     <groupId>pl</groupId>
     <artifactId>inbox-outbox-quarkus</artifactId>
     <version>${inbox-outbox.version}</version>
</dependency>
```
</details>

<details>
    <summary>Konfiguracja beanów</summary>

Zalecana jest implementacja interfejsów `LoadMessages`, `SaveMessage` oraz `DeleteMessage`.
Domyślne implementacje działają na podstawie repozytorium in-memory.


</details>

### Przykład użycia

Obsługa eventów z inboxa/outboxa:

```java
import org.springframework.context.event.EventListener;

@ApplicationScoped
public class SomeEventHandlerService {

    @Inject
    private final InboxOutbox inboxOutbox;

    @EventListener(SomeEvent.class)
    public void handleEvent(SomeEvent event) {
        SomeInboxOutboxEvent inboxEvent = event.toInboxOutboxEvent();
        inboxOutbox.publish(inboxEvent);
    }
}

```

Obsługa eventu wysłanego na inboxa/outboxa:

```java
@ApplicationScoped
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
