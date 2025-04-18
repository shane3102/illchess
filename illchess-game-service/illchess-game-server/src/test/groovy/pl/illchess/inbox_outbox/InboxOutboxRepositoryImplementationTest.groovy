package pl.illchess.inbox_outbox

import org.awaitility.Awaitility
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import pl.illchess.game.adapter.game.command.in.rest.dto.InitializeNewGameRequest
import pl.illchess.game.adapter.game.command.in.rest.dto.InitializedGameResponse
import pl.illchess.game.adapter.game.command.in.rest.dto.ResignGameRequest
import pl.illchess.game.adapter.game.query.in.outbox.dto.ObtainGameFinishedOutboxMessage
import pl.illchess.game.domain.commons.exception.DomainException

import java.time.Duration
import java.util.concurrent.TimeUnit

class InboxOutboxRepositoryImplementationTest extends InboxOutboxRepositoryImplementationSpecification {

    def "should be saved as inbox-outbox event on failing"() {
        given:
        Mockito.doThrow(new DomainException("lol") {})
                .when(boardGameFinishedQueryPort)
                .findById(Mockito.any(UUID))

        def username1 = generateRandomName()
        def username2 = generateRandomName()

        def initializeBoardResponse = joinOrInitializeGame(new InitializeNewGameRequest(username1))
        assert initializeBoardResponse.status == HttpStatus.OK.value()
        def initializedBoard = parseJson(initializeBoardResponse, InitializedGameResponse)

        assert joinOrInitializeGame(new InitializeNewGameRequest(username2)).status == HttpStatus.OK.value()

        when:
        try {
            resignGame(new ResignGameRequest(initializedBoard.id(), username1))
        } catch (Exception ignore) {
        }

        then:
        Awaitility.await().pollInterval(Duration.ofSeconds(1)).atMost(5, TimeUnit.SECONDS)
                .untilAsserted {
                    List<ObtainGameFinishedOutboxMessage> inboxOutboxMessagesOfBoardFinished = loadMessages.loadLatestByTypeNonExpired(
                            ObtainGameFinishedOutboxMessage.class.toString(),
                            100,
                            100
                    ).collect { it as ObtainGameFinishedOutboxMessage }

                    inboxOutboxMessagesOfBoardFinished.size() == 1
                    inboxOutboxMessagesOfBoardFinished[0].gameId() == initializedBoard.id()
                    inboxOutboxMessagesOfBoardFinished[0].id != null
                    inboxOutboxMessagesOfBoardFinished[0].occurredOn != null
                    inboxOutboxMessagesOfBoardFinished[0].retryCount >= 0
                }
    }
}
