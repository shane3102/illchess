package pl.illchess.player_info.server.dev

import java.util.UUID

abstract class Specification {

    fun randomString() = "random.string.${UUID.randomUUID()}"

}