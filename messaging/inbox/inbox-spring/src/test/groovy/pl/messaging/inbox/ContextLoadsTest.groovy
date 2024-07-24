package pl.messaging.inbox

import org.springframework.beans.factory.annotation.Autowired
import pl.messaging.inbox.aggregator.Inbox

class ContextLoadsTest extends SpecificationIT {

    @Autowired
    Inbox inbox

    def 'context loads simple test'() {
        expect:
        inbox != null
    }
}
