package pl.illchess.board.on_game_finished

import org.springframework.mock.web.MockHttpServletResponse
import pl.illchess.SpecificationIT
import pl.illchess.adapter.board.command.in.rest.dto.InitializeNewBoardRequest
import pl.illchess.adapter.board.command.in.rest.dto.ResignGameRequest

abstract class OnGameFinishedSpecification extends SpecificationIT {

    private static final def BOARD_PATH = "/api/board"

    protected MockHttpServletResponse joinOrInitializeGame(InitializeNewBoardRequest initializeNewBoardRequest) {
        putJson("$BOARD_PATH/join-or-initialize", initializeNewBoardRequest)
    }

    protected MockHttpServletResponse resignGame(ResignGameRequest request) {
        putJson("$BOARD_PATH/resign", request)
    }

}
