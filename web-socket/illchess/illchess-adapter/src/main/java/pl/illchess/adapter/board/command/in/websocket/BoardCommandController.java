package pl.illchess.adapter.board.command.in.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import pl.illchess.adapter.board.command.in.websocket.dto.AcceptDrawRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.CheckLegalMovesRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.InitializeNewBoardRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.InitializedBoardResponse;
import pl.illchess.adapter.board.command.in.websocket.dto.LegalMovesResponse;
import pl.illchess.adapter.board.command.in.websocket.dto.MovePieceRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.ProposeDrawRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.RejectDrawRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.ResignGameRequest;
import pl.illchess.application.board.command.in.AcceptDrawUseCase;
import pl.illchess.application.board.command.in.CheckIfCheckmateOrStalemateUseCase;
import pl.illchess.application.board.command.in.CheckIfCheckmateOrStalemateUseCase.CheckIsCheckmateOrStaleMateCmd;
import pl.illchess.application.board.command.in.CheckLegalityMoveUseCase;
import pl.illchess.application.board.command.in.JoinOrInitializeNewGameUseCase;
import pl.illchess.application.board.command.in.MovePieceUseCase;
import pl.illchess.application.board.command.in.ProposeDrawUseCase;
import pl.illchess.application.board.command.in.RejectDrawUseCase;
import pl.illchess.application.board.command.in.ResignGameUseCase;
import pl.illchess.domain.board.event.BoardPiecesLocationsUpdated;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;

import java.util.Set;

import static org.springframework.http.HttpStatus.OK;

@Controller
@RequiredArgsConstructor
public class BoardCommandController implements BoardCommandApi {

    private final MovePieceUseCase movePieceUseCase;
    private final CheckLegalityMoveUseCase checkLegalityMoveUseCase;
    private final JoinOrInitializeNewGameUseCase joinOrInitializeNewGameUseCase;
    private final CheckIfCheckmateOrStalemateUseCase checkIfCheckmateOrStalemateUseCase;
    private final ResignGameUseCase resignGameUseCase;
    private final ProposeDrawUseCase proposeDrawUseCase;
    private final RejectDrawUseCase rejectDrawUseCase;
    private final AcceptDrawUseCase acceptDrawUseCase;

    @Override
    @EventListener(BoardPiecesLocationsUpdated.class)
    public void checkGameState(BoardPiecesLocationsUpdated boardUpdated) {
        CheckIsCheckmateOrStaleMateCmd cmd = new CheckIsCheckmateOrStaleMateCmd(boardUpdated.boardId().uuid());
        checkIfCheckmateOrStalemateUseCase.checkIfCheckmateOrStalemate(cmd);
    }

    @Override
    public ResponseEntity<InitializedBoardResponse> initializeNewBoard(InitializeNewBoardRequest initializeNewBoardRequest) {
        BoardId newBoardId = joinOrInitializeNewGameUseCase.joinOrInitializeNewGame(initializeNewBoardRequest.toCmd());
        InitializedBoardResponse response = new InitializedBoardResponse(newBoardId.uuid());
        return ResponseEntity.ok(response);
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

    @Override
    public ResponseEntity<Void> resignGame(ResignGameRequest request) {
        resignGameUseCase.resignGame(request.toCmd());
        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<Void> proposeDraw(ProposeDrawRequest request) {
        proposeDrawUseCase.proposeDraw(request.toCmd());
        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<Void> rejectDraw(RejectDrawRequest request) {
        rejectDrawUseCase.rejectDraw(request.toCmd());
        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<Void> acceptDraw(AcceptDrawRequest request) {
        acceptDrawUseCase.acceptDraw(request.toCmd());
        return new ResponseEntity<>(OK);
    }
}
