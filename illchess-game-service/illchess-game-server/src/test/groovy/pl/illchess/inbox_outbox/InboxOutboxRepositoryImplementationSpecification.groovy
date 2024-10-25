package pl.illchess.inbox_outbox

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.mock.web.MockHttpServletResponse
import pl.illchess.SpecificationIT
import pl.illchess.game.adapter.board.command.in.rest.dto.InitializeNewBoardRequest
import pl.illchess.game.adapter.board.command.in.rest.dto.ResignGameRequest
import pl.illchess.game.application.board.query.out.BoardGameFinishedQueryPort
import pl.messaging.repository.LoadMessages

abstract class InboxOutboxRepositoryImplementationSpecification extends SpecificationIT {

    @Autowired
    protected LoadMessages loadMessages

    @SpyBean
    BoardGameFinishedQueryPort boardGameFinishedQueryPort

    private static final def BOARD_PATH = "/api/board"

    protected MockHttpServletResponse joinOrInitializeGame(InitializeNewBoardRequest initializeNewBoardRequest) {
        putJson("$BOARD_PATH/join-or-initialize", initializeNewBoardRequest)
    }

    protected MockHttpServletResponse resignGame(ResignGameRequest request) {
        putJson("$BOARD_PATH/resign", request)
    }

}
