package pl.illchess.player_info.application.player.query.out

import pl.illchess.player_info.application.commons.query.model.Page
import pl.illchess.player_info.application.player.query.out.model.PlayerView

interface PlayerViewQueryPort {
    fun findHighestRatedPlayersPageable(pageNumber: Int, pageSize: Int): Page<PlayerView>
}