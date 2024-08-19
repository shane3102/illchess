package pl.illchess.player_info.domain.game.model

import pl.illchess.player_info.domain.game.exception.NoColorOfGivenValueException

enum class PieceColor {
    WHITE, BLACK;

    companion object {
        fun of(value: String): PieceColor {
            try {
                return PieceColor.valueOf(value)
            } catch (e: IllegalArgumentException) {
                throw NoColorOfGivenValueException(value)
            }
        }
    }
}