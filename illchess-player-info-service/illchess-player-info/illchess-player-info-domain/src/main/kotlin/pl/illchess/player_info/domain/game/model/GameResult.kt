package pl.illchess.player_info.domain.game.model

import pl.illchess.player_info.domain.game.exception.GameResultNotRecognisedException
import pl.illchess.player_info.domain.game.model.PieceColor.BLACK
import pl.illchess.player_info.domain.game.model.PieceColor.WHITE
import pl.illchess.player_info.domain.player.model.PlayerGameResult
import pl.illchess.player_info.domain.player.model.PlayerGameResult.DRAWN
import pl.illchess.player_info.domain.player.model.PlayerGameResult.LOST
import pl.illchess.player_info.domain.player.model.PlayerGameResult.WON

enum class GameResult {
    WHITE_WON,
    DRAW,
    BLACK_WON;

    fun forPlayerByColor(color: PieceColor): PlayerGameResult {
        return when (color) {
            WHITE -> when (this) {
                WHITE_WON -> WON
                DRAW -> DRAWN
                BLACK_WON -> LOST
            }

            BLACK -> when (this) {
                WHITE_WON -> LOST
                DRAW -> DRAWN
                BLACK_WON -> WON
            }
        }
    }

    companion object {
        fun of(value: String): GameResult {
            try {
                return GameResult.valueOf(value)
            } catch (e: IllegalArgumentException) {
                throw GameResultNotRecognisedException(value)
            }
        }
    }
}