package pl.illchess.board.on_game_finished

import org.springframework.http.HttpStatus
import pl.illchess.adapter.board.command.in.rest.dto.InitializeNewBoardRequest
import pl.illchess.adapter.board.command.in.rest.dto.InitializedBoardResponse
import pl.illchess.adapter.board.command.in.rest.dto.ResignGameRequest
import pl.illchess.application.board.query.out.model.BoardGameFinishedView

import java.time.Duration

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.awaitility.Awaitility.await
import static java.util.concurrent.TimeUnit.SECONDS

class OnGameFinishedTest extends OnGameFinishedSpecification {

    def "should send info on game finished"() {
        given:
        def username1 = generateRandomName()
        def username2 = generateRandomName()

        def initializeBoardResponse = joinOrInitializeGame(new InitializeNewBoardRequest(username1))
        assert initializeBoardResponse.status == HttpStatus.OK.value()
        def initializedBoard = parseJson(initializeBoardResponse, InitializedBoardResponse)

        assert joinOrInitializeGame(new InitializeNewBoardRequest(username2)).status == HttpStatus.OK.value()

        when:
        assert resignGame(new ResignGameRequest(initializedBoard.id(), username1)).status == HttpStatus.OK.value()

        then:
        await().pollInterval(Duration.ofSeconds(1)).atMost(5, SECONDS)
            .untilAsserted {
                verify(rabbitTemplate, times(1)).convertAndSend(anyString(), any(BoardGameFinishedView))
            }
    }

}
