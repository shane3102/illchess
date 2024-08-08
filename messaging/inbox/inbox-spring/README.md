# Inbox - Spring

Biblioteka wspierająca asynchroniczne odbieranie komunikatów pomiędzy serwisami z wykorzystaniem wzorca Inbox dla aplikacji opartych na Springu.

### Konfiguracja
<details>
    <summary>Dependency - konfiguracja maven</summary>

```
<dependency>
    <groupId>pl.illchess.messaging</groupId>
    <artifactId>inbox-spring</artifactId>
    <version>${inbox.version}</version>
</dependency>
```
</details>

<details>
    <summary>Konfiguracja beanów Spring</summary>
    
Zalecana jest implementacja interfejsów `LoadInboxMessages`, `SaveInboxMessage` oraz `DeleteInboxMessage`. 
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

#### Obsługa eventów z inboxa:

Wysyłanie eventów na inboxa:
```java
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SomeEventHandlerService {

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

@Service
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
