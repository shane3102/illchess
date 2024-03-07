package pl.illchess.adapter.board.command.in.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.illchess.adapter.board.command.in.websocket.dto.InitializeNewBoardRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.LegalMovesResponse;
import pl.illchess.adapter.board.command.in.websocket.dto.CheckLegalMovesRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.MovePieceRequest;
import pl.illchess.application.board.command.in.CheckIfCheckmateOrStalemateUseCase;
import pl.illchess.application.board.command.in.CheckIfCheckmateOrStalemateUseCase.CheckIsCheckmateOrStaleMateCmd;
import pl.illchess.application.board.command.in.CheckLegalityMoveUseCase;
import pl.illchess.application.board.command.in.InitializeNewBoardUseCase;
import pl.illchess.application.board.command.in.MovePieceUseCase;
import pl.illchess.domain.board.event.BoardPiecesLocationsUpdated;
import pl.illchess.domain.board.model.square.Square;

import java.util.Set;

import static org.springframework.http.HttpStatus.OK;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/board")
@CrossOrigin(origins = {"http://localhost:4200", "http://192.168.1.49:4200"})
public class BoardCommandController implements BoardCommandApi {

    private final MovePieceUseCase movePieceUseCase;
    private final CheckLegalityMoveUseCase checkLegalityMoveUseCase;
    private final InitializeNewBoardUseCase initializeNewBoardUseCase;
    private final CheckIfCheckmateOrStalemateUseCase checkIfCheckmateOrStalemateUseCase;

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
    public ResponseEntity<Void> movePiece(
        MovePieceRequest movePieceRequest
    ) {
        movePieceUseCase.movePiece(movePieceRequest.toCmd());
        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<LegalMovesResponse> checkLegalityOfMove(
        CheckLegalMovesRequest request
    ) {
        Set<Square> response = checkLegalityMoveUseCase.checkLegalityOfMove(request.toCmd());
        return ResponseEntity.ok(new LegalMovesResponse(response));
    }
}
