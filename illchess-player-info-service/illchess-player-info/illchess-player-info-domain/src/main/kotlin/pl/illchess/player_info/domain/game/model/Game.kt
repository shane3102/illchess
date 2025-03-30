package pl.illchess.player_info.domain.game.model

import pl.illchess.player_info.domain.game.command.ObtainNewGame
import pl.illchess.player_info.domain.game.model.PieceColor.BLACK
import pl.illchess.player_info.domain.game.model.PieceColor.WHITE

data class Game(
    val id: GameId,
    val whitePlayerGameInfo: PlayerGameInfo,
    val blackPlayerGameInfo: PlayerGameInfo,
    val gameResult: GameResult,
    val gameResultCause: GameResultCause,
    val endTime: EndTime,
    val performedMoves: List<PerformedMove>
) {

    companion object {
        fun generateNewGame(command: ObtainNewGame): Game {
            val whiteUser = command.whitePlayer
            val blackUser = command.blackPlayer

            val whiteUserCurrentRanking = whiteUser.currentRanking
            val blackUserCurrentRanking = blackUser.currentRanking

            val whiteResult = command.gameResult.forPlayerByColor(WHITE)
            val blackResult = command.gameResult.forPlayerByColor(BLACK)

            val whiteUserGameInfo = whiteUserCurrentRanking.establishPlayerGameInfo(
                whiteUser, blackUserCurrentRanking, whiteResult
            )
            val blackUserGameInfo = blackUserCurrentRanking.establishPlayerGameInfo(
                blackUser, whiteUserCurrentRanking, blackResult
            )

            whiteUser.applyNewRanking(whiteUserGameInfo)
            blackUser.applyNewRanking(blackUserGameInfo)

            return Game(
                command.gameId,
                whiteUserGameInfo,
                blackUserGameInfo,
                command.gameResult,
                command.gameResultCause,
                command.endTime,
                command.performedMoves
            )
        }
    }
}