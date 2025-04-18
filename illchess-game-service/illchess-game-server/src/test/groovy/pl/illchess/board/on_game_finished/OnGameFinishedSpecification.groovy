package pl.illchess.board.on_game_finished

import org.springframework.mock.web.MockHttpServletResponse
import pl.illchess.SpecificationIT
import pl.illchess.game.adapter.game.command.in.rest.dto.InitializeNewGameRequest
import pl.illchess.game.adapter.game.command.in.rest.dto.ResignGameRequest

abstract class OnGameFinishedSpecification extends SpecificationIT {

    private static final def GAME_PATH = "/api/game"

    protected MockHttpServletResponse joinOrInitializeGame(InitializeNewGameRequest initializeNewBoardRequest) {
        putJson("$GAME_PATH/join-or-initialize", initializeNewBoardRequest)
    }

    protected MockHttpServletResponse resignGame(ResignGameRequest request) {
        putJson("$GAME_PATH/resign", request)
    }

}
