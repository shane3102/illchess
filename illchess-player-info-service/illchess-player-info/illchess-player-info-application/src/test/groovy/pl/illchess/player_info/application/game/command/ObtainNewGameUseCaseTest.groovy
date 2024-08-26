package pl.illchess.player_info.application.game.command

import pl.illchess.player_info.application.UnitTestSpecification
import pl.illchess.player_info.application.game.command.in.ObtainNewGameUseCase
import pl.illchess.player_info.domain.game.model.GameId
import pl.illchess.player_info.domain.user.model.User
import pl.illchess.player_info.domain.user.model.UserId
import pl.illchess.player_info.domain.user.model.UserRankingPoints
import pl.illchess.player_info.domain.user.model.Username

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
                winningPieceColor,
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
        game.winningPieceColor.name() == winningPieceColor
        game.performedMoves.size() == performedMoves.size()

        userWhite != null
        userWhite.currentRanking.value != userWhiteStartingPoints
        userBlack != null
        userBlack.currentRanking.value != userBlackStartingPoints

        where:
        winningPieceColor | userWhiteStartingPoints | userBlackStartingPoints | performedMoves
        "WHITE"           | 0                       | 0                       | []

    }
}
