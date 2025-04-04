package pl.illchess.player_info.application.game.command

import java.time.LocalDateTime
import pl.illchess.player_info.application.UnitTestSpecification
import pl.illchess.player_info.application.game.command.in.ObtainNewGameUseCase
import pl.illchess.player_info.application.game.command.in.ObtainNewGameUseCase.PerformedMoveCmd
import pl.illchess.player_info.domain.game.model.GameId
import pl.illchess.player_info.domain.game.model.GameResult
import pl.illchess.player_info.domain.game.model.PieceColor
import pl.illchess.player_info.domain.player.model.Player
import pl.illchess.player_info.domain.player.model.PlayerId
import pl.illchess.player_info.domain.player.model.PlayerRankingPoints
import pl.illchess.player_info.domain.player.model.Username
import static pl.illchess.player_info.domain.game.model.GameResult.BLACK_WON
import static pl.illchess.player_info.domain.game.model.GameResult.DRAW
import static pl.illchess.player_info.domain.game.model.GameResult.WHITE_WON
import static pl.illchess.player_info.domain.game.model.GameResultCause.CHECKMATE
import static pl.illchess.player_info.domain.game.model.GameResultCause.PLAYER_AGREEMENT
import static pl.illchess.player_info.domain.game.model.GameResultCause.RESIGNATION
import static pl.illchess.player_info.domain.game.model.PieceColor.BLACK
import static pl.illchess.player_info.domain.game.model.PieceColor.WHITE

class ObtainNewGameUseCaseTest extends UnitTestSpecification {

    ObtainNewGameUseCase obtainNewGameUseCase = gameManager

    def "should change ranking of each game participant and save new game to database"() {
        given:
        def gameId = UUID.randomUUID()

        def userWhiteUsername = new Username(generateRandomString())
        saveUser.save(new Player(
                new PlayerId(UUID.randomUUID()),
                userWhiteUsername,
                new PlayerRankingPoints(userWhiteStartingPoints)
        ))

        def userBlackUsername = new Username(generateRandomString())
        saveUser.save(new Player(
                new PlayerId(UUID.randomUUID()),
                userBlackUsername,
                new PlayerRankingPoints(userBlackStartingPoints)
        ))

        def cmd = new ObtainNewGameUseCase.ObtainNewGameCmd(
                gameId,
                userWhiteUsername.text,
                userBlackUsername.text,
                gameResult.name(),
                gameResultCause.name(),
                LocalDateTime.now(),
                performedMoves
        )

        when:
        obtainNewGameUseCase.obtainNewGame(cmd)

        then:
        def game = loadGame.load(new GameId(gameId))
        def userWhite = loadUser.load(userWhiteUsername)
        def userBlack = loadUser.load(userBlackUsername)

        game != null
        game.id.uuid == gameId
        game.gameResult == gameResult
        game.performedMoves.size() == performedMoves.size()
        game.performedMoves.every { performedMove ->
            performedMoves.any {
                performedMove.startSquare.name() == it.startSquare
                performedMove.endSquare.name() == it.endSquare
                performedMove.color.name() == it.color
                performedMove.stringValue.value == it.stringValue
            }
        }

        userWhite != null
        checkPointChangeByResult(userWhite, userWhiteStartingPoints, userBlackStartingPoints, game.gameResult, WHITE)

        userBlack != null
        checkPointChangeByResult(userBlack, userBlackStartingPoints, userWhiteStartingPoints, game.gameResult, BLACK)

        game.whitePlayerGameInfo.rankingPointsBeforeGame.value == userWhiteStartingPoints
        game.blackPlayerGameInfo.rankingPointsBeforeGame.value == userBlackStartingPoints

        where:
        gameResult | gameResultCause  | userWhiteStartingPoints | userBlackStartingPoints | performedMoves
        WHITE_WON  | CHECKMATE        | 800                     | 800                     | [new PerformedMoveCmd("A2", "A5", "pA5", "WHITE")]
        BLACK_WON  | RESIGNATION      | 800                     | 800                     | []
        DRAW       | PLAYER_AGREEMENT | 800                     | 800                     | []

    }

    boolean checkPointChangeByResult(Player user, int startingPoints, int enemyStartingPoints, GameResult gameResult, PieceColor playerColor) {
        if (playerColor == WHITE) {
            if (gameResult == WHITE_WON) {
                return user.currentRanking.value > startingPoints
            }
            if (gameResult == BLACK_WON) {
                return user.currentRanking.value < startingPoints
            }
        }
        if (playerColor == BLACK) {
            if (gameResult == BLACK_WON) {
                return user.currentRanking.value > startingPoints
            }
            if (gameResult == WHITE_WON) {
                return user.currentRanking.value < startingPoints
            }
        }
        if (gameResult == DRAW) {
            if (startingPoints > enemyStartingPoints) {
                return user.currentRanking.value <= startingPoints
            }
            if (startingPoints < enemyStartingPoints) {
                return user.currentRanking.value >= startingPoints
            }
            if (startingPoints == enemyStartingPoints) {
                return user.currentRanking.value == startingPoints
            }

        }
        false
    }
}
