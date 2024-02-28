package pl.illchess.adapter.board.command.in.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import pl.illchess.adapter.board.command.in.websocket.request.InitializeNewBoardRequest;
import pl.illchess.adapter.board.command.in.websocket.request.MovePieceRequest;
import pl.illchess.application.board.command.in.CheckIfCheckmateOrStalemateUseCase;
import pl.illchess.application.board.command.in.CheckIfCheckmateOrStalemateUseCase.CheckIsCheckmateOrStaleMateCmd;
import pl.illchess.application.board.command.in.InitializeNewBoardUseCase;
import pl.illchess.application.board.command.in.MovePieceUseCase;
import pl.illchess.domain.board.event.BoardPiecesLocationsUpdated;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BoardCommandController implements BoardCommandApi {

    private final MovePieceUseCase movePieceUseCase;
    private final InitializeNewBoardUseCase initializeNewBoardUseCase;
    private final CheckIfCheckmateOrStalemateUseCase checkIfCheckmateOrStalemateUseCase;

    @Override
    @MessageMapping("/board/move-piece")
    public void movePiece(MovePieceRequest movePieceRequest) {
        movePieceUseCase.movePiece(movePieceRequest.toCmd());
    }

    @Override
    @MessageMapping("/board/create")
    public void initializeNewBoard(InitializeNewBoardRequest initializeNewBoardRequest) {
        initializeNewBoardUseCase.initializeNewGame(initializeNewBoardRequest.toCmd());
    }

    @Override
    @EventListener(BoardPiecesLocationsUpdated.class)
    public void checkGameState(BoardPiecesLocationsUpdated boardUpdated) {
        CheckIsCheckmateOrStaleMateCmd cmd = new CheckIsCheckmateOrStaleMateCmd(boardUpdated.boardId().uuid());
        checkIfCheckmateOrStalemateUseCase.checkIfCheckmateOrStalemate(cmd);
    }
}
