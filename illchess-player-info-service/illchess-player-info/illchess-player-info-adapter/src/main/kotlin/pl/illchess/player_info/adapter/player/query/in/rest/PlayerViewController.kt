package pl.illchess.player_info.adapter.player.query.`in`.rest

import pl.illchess.player_info.application.player.query.out.PlayerViewQueryPort

class PlayerViewController(
    private val playerViewQueryPort: PlayerViewQueryPort
): PlayerViewApi {
    override fun ranking(
        page: Int,
        pageSize: Int
    ) = playerViewQueryPort.findHighestRatedPlayersPageable(page, pageSize)
}