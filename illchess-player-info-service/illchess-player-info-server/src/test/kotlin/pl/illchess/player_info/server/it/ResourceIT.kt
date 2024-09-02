package pl.illchess.player_info.server.it

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusIntegrationTest
import pl.illchess.player_info.server.dev.ResourceTest

@QuarkusIntegrationTest
@QuarkusTestResource(
    value = SpecificationResourceIT::class,
    restrictToAnnotatedClass = true
)
class ResourceIT : ResourceTest()