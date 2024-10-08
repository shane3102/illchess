package pl.illchess.player_info.server.dev

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.test.common.QuarkusTestResource
import java.util.UUID

@QuarkusTestResource(value = SpecificationResource::class)
abstract class Specification {

    protected val objectMapper: ObjectMapper = ObjectMapper().findAndRegisterModules()

    fun randomString() = "random.string.${UUID.randomUUID()}"

}