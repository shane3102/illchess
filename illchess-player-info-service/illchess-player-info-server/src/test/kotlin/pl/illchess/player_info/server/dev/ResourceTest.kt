package pl.illchess.player_info.server.dev

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.illchess.player_info.server.it.SpecificationResourceIT

@QuarkusTest
@QuarkusTestResource(
    value = SpecificationResourceIT::class,
    restrictToAnnotatedClass = true
)
open class ResourceTest {

    @Test
    fun lol() {
        Assertions.assertTrue(true)
    }
}