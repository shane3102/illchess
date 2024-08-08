# Inbox - Quarkus

Biblioteka wspierająca asynchroniczne odbieranie komunikatów pomiędzy serwisami z wykorzystaniem wzorca Inbox dla aplikacji opartych na Quarkusie.

### Konfiguracja
<details>
    <summary>Dependency - konfiguracja maven</summary>

```
<dependency>
     <groupId>pl</groupId>
     <artifactId>inbox-quarkus</artifactId>
     <version>${inbox.version}</version>
</dependency>
```
</details>

<details>
    <summary>Konfiguracja beanów</summary>

Zalecana jest implementacja interfejsów `LoadInboxMessages`, `SaveInboxMessage` oraz `DeleteInboxMessage`.
Domyślne implementacje działają na podstawie repozytorium in-memory.


</details>

### Przykład użycia

#### Obsługa eventów z inboxa:

```java
import org.springframework.context.event.EventListener;

@ApplicationScoped
public class SomeEventHandlerService {

    @Inject
    private final Inbox inbox;

    @EventListener(SomeEvent.class)
    public void handleEvent(SomeEvent event) {
        SomeInboxEvent inboxEvent = event.toInboxEvent();
        inbox.publish(inboxEvent);
    }
}

```

Obsługa eventu wysłanego na inboxa:

```java
import pl.messaging.inbox.annotation.InboxAwareComponent;

@ApplicationScoped
@InboxAwareComponent
public class SomeHandlerService {

    @InboxListener(
            retryCount = 2,
            batchSize = 10,
            cron = "* * * * * *"
    )
    public void handleSomeInboxEvent(SomeInboxEvent someEvent) {
        // implementation
    }
}
```
