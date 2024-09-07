package pl.illchess.player_info.domain.game.model

import pl.illchess.player_info.domain.game.command.ObtainNewGame

data class Game(
    val id: GameId,
    val whiteUserGameInfo: UserGameInfo,
    val blackUserGameInfo: UserGameInfo,
    val winningPieceColor: PieceColor,
    val endTime: EndTime,
    val performedMoves: List<PerformedMove>
) {

    companion object {
        fun generateNewGame(command: ObtainNewGame): Game {
            val recalculateRankingWhite = command.whiteUser.recalculateRanking(command)
            val recalculateRankingBlack = command.blackUser.recalculateRanking(command)

            return Game(
                command.gameId,
                recalculateRankingWhite,
                recalculateRankingBlack,
                command.winningPieceColor,
                command.endTime,
                command.performedMoves
            )
        }
    }
}