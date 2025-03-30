package pl.illchess.player_info.domain.game.model

import pl.illchess.player_info.domain.game.exception.GameResultCauseNotRecognisedException

enum class GameResultCause {
    // some player won
    CHECKMATE,
    RESIGNATION,

    // draw,
    STALEMATE,
    INSUFFICIENT_MATERIAL,
    PLAYER_AGREEMENT;

    companion object {
        fun of(value: String): GameResultCause {
            try {
                return GameResultCause.valueOf(value)
            } catch (e: IllegalArgumentException) {
                throw GameResultCauseNotRecognisedException(value)
            }
        }
    }
}