package pl.messaging.inbox.quarkus.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import pl.messaging.inbox.quarkus.runtime.aggregator.Inbox;

@QuarkusTest
public class InboxQuarkusResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/inbox-quarkus")
                .then()
                .statusCode(200)
                .body(is("Hello inbox-quarkus"));
    }
}
