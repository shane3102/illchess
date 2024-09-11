package pl.illchess.player_info.application.game.command

import pl.illchess.player_info.application.UnitTestSpecification
import pl.illchess.player_info.application.game.command.in.ObtainNewGameUseCase
import pl.illchess.player_info.application.game.command.in.ObtainNewGameUseCase.PerformedMoveCmd
import pl.illchess.player_info.domain.game.model.GameId
import pl.illchess.player_info.domain.game.model.GameResult
import pl.illchess.player_info.domain.game.model.PieceColor
import pl.illchess.player_info.domain.user.model.User
import pl.illchess.player_info.domain.user.model.UserId
import pl.illchess.player_info.domain.user.model.UserRankingPoints
import pl.illchess.player_info.domain.user.model.Username

import java.time.LocalDateTime

import static pl.illchess.player_info.domain.game.model.GameResult.*
import static pl.illchess.player_info.domain.game.model.PieceColor.BLACK
import static pl.illchess.player_info.domain.game.model.PieceColor.WHITE

class ObtainNewGameUseCaseTest extends UnitTestSpecification {

    ObtainNewGameUseCase obtainNewGameUseCase = gameManager

    def "should change ranking of each game participant and save new game to database"() {
        given:
        def gameId = UUID.randomUUID()

        def userWhiteUsername = new Username(generateRandomString())
        saveUser.save(new User(
                new UserId(UUID.randomUUID()),
                userWhiteUsername,
                new UserRankingPoints(userWhiteStartingPoints)
        ))

        def userBlackUsername = new Username(generateRandomString())
        saveUser.save(new User(
                new UserId(UUID.randomUUID()),
                userBlackUsername,
                new UserRankingPoints(userBlackStartingPoints)
        ))

        def cmd = new ObtainNewGameUseCase.ObtainNewGameCmd(
                gameId,
                userWhiteUsername.text,
                userBlackUsername.text,
                gameResult.name(),
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

        where:
        gameResult | userWhiteStartingPoints | userBlackStartingPoints | performedMoves
        WHITE_WON  | 800                     | 800                     | [new PerformedMoveCmd("A2", "A5", "pA5", "WHITE")]
        BLACK_WON  | 800                     | 800                     | []
        DRAW       | 800                     | 800                     | []

    }

    boolean checkPointChangeByResult(User user, int startingPoints, int enemyStartingPoints, GameResult gameResult, PieceColor playerColor) {
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
