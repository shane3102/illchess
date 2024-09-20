package pl.illchess.player_info.server.dev

import io.quarkus.test.common.QuarkusTestResource
import java.util.UUID

@QuarkusTestResource(value = SpecificationResource::class)
abstract class Specification {

    fun randomString() = "random.string.${UUID.randomUUID()}"

}