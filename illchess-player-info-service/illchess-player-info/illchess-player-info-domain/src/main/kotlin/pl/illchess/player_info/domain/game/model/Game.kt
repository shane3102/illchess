package pl.illchess.player_info.domain.game.model

import pl.illchess.player_info.domain.game.command.ObtainNewGame
import pl.illchess.player_info.domain.game.model.PieceColor.BLACK
import pl.illchess.player_info.domain.game.model.PieceColor.WHITE

data class Game(
    val id: GameId,
    val whiteUserGameInfo: UserGameInfo,
    val blackUserGameInfo: UserGameInfo,
    val gameResult: GameResult,
    val endTime: EndTime,
    val performedMoves: List<PerformedMove>
) {

    companion object {
        fun generateNewGame(command: ObtainNewGame): Game {
            val whiteUser = command.whiteUser
            val blackUser = command.blackUser

            val whiteUserCurrentRanking = whiteUser.currentRanking
            val blackUserCurrentRanking = blackUser.currentRanking

            val whiteResult = command.gameResult.forPlayerByColor(WHITE)
            val blackResult = command.gameResult.forPlayerByColor(BLACK)

            val whiteUserGameInfo = whiteUserCurrentRanking.establishUserGameInfo(
                whiteUser, blackUserCurrentRanking, whiteResult
            )
            val blackUserGameInfo = blackUserCurrentRanking.establishUserGameInfo(
                blackUser, whiteUserCurrentRanking, blackResult
            )

            whiteUser.applyNewRanking(whiteUserGameInfo)
            blackUser.applyNewRanking(blackUserGameInfo)

            return Game(
                command.gameId,
                whiteUserGameInfo,
                blackUserGameInfo,
                command.gameResult,
                command.endTime,
                command.performedMoves
            )
        }
    }
}