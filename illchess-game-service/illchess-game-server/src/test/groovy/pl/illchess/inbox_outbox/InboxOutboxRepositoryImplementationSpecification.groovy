package pl.illchess.inbox_outbox

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import pl.illchess.SpecificationIT
import pl.illchess.game.adapter.game.command.in.rest.dto.InitializeNewGameRequest
import pl.illchess.game.adapter.game.command.in.rest.dto.ResignGameRequest
import pl.illchess.game.application.game.query.out.GameFinishedQueryPort
import pl.shane3102.messaging.repository.LoadMessages

abstract class InboxOutboxRepositoryImplementationSpecification extends SpecificationIT {

    @Autowired
    protected LoadMessages loadMessages

    @MockitoSpyBean
    GameFinishedQueryPort boardGameFinishedQueryPort

    private static final def GAME_PATH = "/api/game"

    protected MockHttpServletResponse joinOrInitializeGame(InitializeNewGameRequest initializeNewBoardRequest) {
        putJson("$GAME_PATH/join-or-initialize", initializeNewBoardRequest)
    }

    protected MockHttpServletResponse resignGame(ResignGameRequest request) {
        putJson("$GAME_PATH/resign", request)
    }

}
