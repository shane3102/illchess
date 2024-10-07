package pl.illchess.inbox_outbox

import org.awaitility.Awaitility
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import pl.illchess.adapter.board.command.in.rest.dto.InitializeNewBoardRequest
import pl.illchess.adapter.board.command.in.rest.dto.InitializedBoardResponse
import pl.illchess.adapter.board.command.in.rest.dto.ResignGameRequest
import pl.illchess.adapter.board.query.in.inbox.dto.ObtainBoardGameFinishedInboxMessage
import pl.illchess.domain.commons.exception.DomainException

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

        def initializeBoardResponse = joinOrInitializeGame(new InitializeNewBoardRequest(username1))
        assert initializeBoardResponse.status == HttpStatus.OK.value()
        def initializedBoard = parseJson(initializeBoardResponse, InitializedBoardResponse)

        assert joinOrInitializeGame(new InitializeNewBoardRequest(username2)).status == HttpStatus.OK.value()

        when:
        try {
            resignGame(new ResignGameRequest(initializedBoard.id(), username1))
        } catch (Exception ignore) {
        }

        then:
        Awaitility.await().pollInterval(Duration.ofSeconds(1)).atMost(5, TimeUnit.SECONDS)
                .untilAsserted {
                    List<ObtainBoardGameFinishedInboxMessage> inboxOutboxMessagesOfBoardFinished = loadMessages.loadLatestByTypeNonExpired(
                            ObtainBoardGameFinishedInboxMessage.class.toString(),
                            100,
                            100
                    ).collect { it as ObtainBoardGameFinishedInboxMessage }

                    inboxOutboxMessagesOfBoardFinished.size() == 1
                    inboxOutboxMessagesOfBoardFinished[0].gameId() == initializedBoard.id()
                    inboxOutboxMessagesOfBoardFinished[0].id != null
                    inboxOutboxMessagesOfBoardFinished[0].occurredOn != null
                    inboxOutboxMessagesOfBoardFinished[0].retryCount >= 0
                }
    }
}
