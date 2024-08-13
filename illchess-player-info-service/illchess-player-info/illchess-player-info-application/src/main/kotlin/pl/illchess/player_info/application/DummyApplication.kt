package pl.illchess.player_info.application

import pl.illchess.player_info.domain.DummyDomain

class DummyApplication {
    fun dummy(): String {
        return DummyDomain().dummy()
    }
}