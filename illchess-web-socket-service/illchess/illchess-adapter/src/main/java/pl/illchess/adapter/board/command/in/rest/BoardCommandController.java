package pl.illchess.adapter.board.command.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import pl.illchess.adapter.board.command.in.rest.dto.AcceptDrawRequest;
import pl.illchess.adapter.board.command.in.rest.dto.AcceptTakingBackMoveRequest;
import pl.illchess.adapter.board.command.in.rest.dto.BoardFenStringResponse;
import pl.illchess.adapter.board.command.in.rest.dto.CheckLegalMovesRequest;
import pl.illchess.adapter.board.command.in.rest.dto.InitializeNewBoardRequest;
import pl.illchess.adapter.board.command.in.rest.dto.InitializedBoardResponse;
import pl.illchess.adapter.board.command.in.rest.dto.LegalMovesResponse;
import pl.illchess.adapter.board.command.in.rest.dto.MovePieceRequest;
import pl.illchess.adapter.board.command.in.rest.dto.ProposeDrawRequest;
import pl.illchess.adapter.board.command.in.rest.dto.ProposeTakingBackMoveRequest;
import pl.illchess.adapter.board.command.in.rest.dto.RejectDrawRequest;
import pl.illchess.adapter.board.command.in.rest.dto.RejectTakingBackMoveRequest;
import pl.illchess.adapter.board.command.in.rest.dto.ResignGameRequest;
import pl.illchess.application.board.command.in.AcceptDrawUseCase;
import pl.illchess.application.board.command.in.AcceptTakingBackLastMoveUseCase;
import pl.illchess.application.board.command.in.CheckLegalityMoveUseCase;
import pl.illchess.application.board.command.in.EstablishFenStringOfBoardUseCase;
import pl.illchess.application.board.command.in.EstablishFenStringOfBoardUseCase.EstablishFenStringOfBoardCmd;
import pl.illchess.application.board.command.in.JoinOrInitializeNewGameUseCase;
import pl.illchess.application.board.command.in.MovePieceUseCase;
import pl.illchess.application.board.command.in.ProposeDrawUseCase;
import pl.illchess.application.board.command.in.ProposeTakeBackMoveUseCase;
import pl.illchess.application.board.command.in.RejectDrawUseCase;
import pl.illchess.application.board.command.in.RejectTakingBackLastMoveUseCase;
import pl.illchess.application.board.command.in.ResignGameUseCase;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.FenBoardString;
import pl.illchess.domain.board.model.square.Square;

import java.util.Set;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@Controller
@RequiredArgsConstructor
public class BoardCommandController implements BoardCommandApi {

    private final MovePieceUseCase movePieceUseCase;
    private final CheckLegalityMoveUseCase checkLegalityMoveUseCase;
    private final JoinOrInitializeNewGameUseCase joinOrInitializeNewGameUseCase;
    private final ResignGameUseCase resignGameUseCase;
    private final ProposeDrawUseCase proposeDrawUseCase;
    private final RejectDrawUseCase rejectDrawUseCase;
    private final AcceptDrawUseCase acceptDrawUseCase;
    private final EstablishFenStringOfBoardUseCase establishFenStringOfBoardUseCase;
    private final ProposeTakeBackMoveUseCase proposeTakeBackMoveUseCase;
    private final AcceptTakingBackLastMoveUseCase acceptTakingBackLastMoveUseCase;
    private final RejectTakingBackLastMoveUseCase rejectTakingBackLastMoveUseCase;

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

    @Override
    public ResponseEntity<BoardFenStringResponse> establishFenString(UUID boardId) {
        FenBoardString fenBoardString = establishFenStringOfBoardUseCase.establishCurrentFenBoardString(
            new EstablishFenStringOfBoardCmd(boardId)
        );
        BoardFenStringResponse result = new BoardFenStringResponse(
            "%s %s %s %s %s %s".formatted(
                fenBoardString.position(),
                fenBoardString.activeColor(),
                fenBoardString.castlingAvailability(),
                fenBoardString.enPassantTargetSquare(),
                fenBoardString.halfMoveClock(),
                fenBoardString.fullMoveNumber()
            )
        );
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> proposeTakingBackMove(ProposeTakingBackMoveRequest request) {
        proposeTakeBackMoveUseCase.proposeTakingBackMove(request.toCmd());
        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<Void> acceptTakingBackMove(AcceptTakingBackMoveRequest request) {
        acceptTakingBackLastMoveUseCase.acceptTakingBackLastMove(request.toCmd());
        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<Void> rejectTakingBackMove(RejectTakingBackMoveRequest request) {
        rejectTakingBackLastMoveUseCase.rejectTakingBackLastMove(request.toCmd());
        return new ResponseEntity<>(OK);
    }
}
