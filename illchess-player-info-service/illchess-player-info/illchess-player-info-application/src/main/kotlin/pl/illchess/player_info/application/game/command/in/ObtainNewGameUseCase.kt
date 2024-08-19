package pl.illchess.player_info.application.game.command.`in`

import pl.illchess.player_info.domain.game.command.ObtainNewGame
import pl.illchess.player_info.domain.game.model.ChessSquare
import pl.illchess.player_info.domain.game.model.GameId
import pl.illchess.player_info.domain.game.model.MoveAlgebraicNotation
import pl.illchess.player_info.domain.game.model.PerformedMove
import pl.illchess.player_info.domain.game.model.PieceColor
import pl.illchess.player_info.domain.user.model.User
import java.util.UUID

interface ObtainNewGameUseCase {

    fun obtainNewGame(cmd: ObtainNewGameCmd)

    data class ObtainNewGameCmd(
        val id: UUID,
        val whiteUsername: String,
        val blackUsername: String,
        val winningPieceColor: String,
        val performedMoves: List<PerformedMoveCmd>
    ) {
        fun toCommand(whitePlayer: User, blackPlayer: User): ObtainNewGame {
            return ObtainNewGame(
                GameId(id),
                whitePlayer,
                blackPlayer,
                PieceColor.valueOf(winningPieceColor),
                performedMoves.map {
                    PerformedMove(
                        ChessSquare.of(it.startSquare),
                        ChessSquare.of(it.endSquare),
                        MoveAlgebraicNotation(it.stringValue),
                        PieceColor.of(it.color)
                    )
                }
            )
        }
    }

    data class PerformedMoveCmd(
        val startSquare: String,
        val endSquare: String,
        val stringValue: String,
        val color: String
    )
}