package pl.illchess.player_info.application.game.query.out

import pl.illchess.player_info.application.commons.query.model.Page
import pl.illchess.player_info.application.game.query.out.model.GameSnippetView

interface GameSnippetViewLatestQueryPort {
    fun findLatestGamesPageable(pageNumber: Int, pageSize: Int): Page<GameSnippetView>
}