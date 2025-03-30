package pl.illchess.player_info.application.game.command.`in`

import java.time.LocalDateTime
import java.util.UUID
import pl.illchess.player_info.domain.game.command.ObtainNewGame
import pl.illchess.player_info.domain.game.model.ChessSquare
import pl.illchess.player_info.domain.game.model.EndTime
import pl.illchess.player_info.domain.game.model.GameId
import pl.illchess.player_info.domain.game.model.GameResult
import pl.illchess.player_info.domain.game.model.GameResultCause
import pl.illchess.player_info.domain.game.model.MoveAlgebraicNotation
import pl.illchess.player_info.domain.game.model.PerformedMove
import pl.illchess.player_info.domain.game.model.PieceColor
import pl.illchess.player_info.domain.player.model.Player

interface ObtainNewGameUseCase {

    fun obtainNewGame(cmd: ObtainNewGameCmd)

    data class ObtainNewGameCmd(
        val id: UUID,
        val whiteUsername: String,
        val blackUsername: String,
        val gameResult: String,
        val gameResultCause: String,
        val endTime: LocalDateTime,
        val performedMoves: List<PerformedMoveCmd>
    ) {
        fun toCommand(whitePlayer: Player, blackPlayer: Player): ObtainNewGame {
            return ObtainNewGame(
                GameId(id),
                whitePlayer,
                blackPlayer,
                GameResult.of(gameResult),
                GameResultCause.of(gameResultCause),
                EndTime(endTime),
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