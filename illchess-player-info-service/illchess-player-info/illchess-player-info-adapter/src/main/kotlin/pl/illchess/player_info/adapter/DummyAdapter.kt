package pl.illchess.player_info.adapter

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import pl.illchess.player_info.application.DummyApplication

@Path("")
class DummyAdapter {


    @GET
    fun dummy(): String {
        return DummyApplication().dummy()
    }
}