package pl.illchess.player_info.server.dev

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@QuarkusTest
@QuarkusTestResource(
    value = SpecificationResource::class,
    restrictToAnnotatedClass = true
)
open class ResourceTest {

    @Test
    fun lol() {
        Assertions.assertTrue(true)
    }
}