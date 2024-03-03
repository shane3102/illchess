package pl.illchess.adapter.board.command.in.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.illchess.adapter.board.command.in.websocket.request.InitializeNewBoardRequest;
import pl.illchess.adapter.board.command.in.websocket.request.LegalMovesResponse;
import pl.illchess.adapter.board.command.in.websocket.request.CheckLegalMovesRequest;
import pl.illchess.adapter.board.command.in.websocket.request.MovePieceRequest;
import pl.illchess.application.board.command.in.CheckIfCheckmateOrStalemateUseCase;
import pl.illchess.application.board.command.in.CheckIfCheckmateOrStalemateUseCase.CheckIsCheckmateOrStaleMateCmd;
import pl.illchess.application.board.command.in.CheckLegalityMoveUseCase;
import pl.illchess.application.board.command.in.InitializeNewBoardUseCase;
import pl.illchess.application.board.command.in.MovePieceUseCase;
import pl.illchess.domain.board.event.BoardPiecesLocationsUpdated;
import pl.illchess.domain.board.model.square.Square;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/board")
@CrossOrigin(origins = "http://localhost:4200")
public class BoardCommandController implements BoardCommandApi {

    private final MovePieceUseCase movePieceUseCase;
    private final CheckLegalityMoveUseCase checkLegalityMoveUseCase;
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

    @Override
    @PutMapping(value = "/legal-moves", produces = "application/json")
    public @ResponseBody ResponseEntity<LegalMovesResponse> checkLegalityOfMove(
        @RequestBody CheckLegalMovesRequest request
    ) {
        Set<Square> response = checkLegalityMoveUseCase.checkLegalityOfMove(request.toCmd());
        return ResponseEntity.ok(new LegalMovesResponse(response));
    }
}
