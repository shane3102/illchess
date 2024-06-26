package pl.illchess.adapter.board.command.in.websocket;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.illchess.adapter.board.command.in.websocket.dto.AcceptDrawRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.AcceptTakingBackMoveRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.BoardFenStringResponse;
import pl.illchess.adapter.board.command.in.websocket.dto.CheckLegalMovesRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.InitializeNewBoardRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.InitializedBoardResponse;
import pl.illchess.adapter.board.command.in.websocket.dto.LegalMovesResponse;
import pl.illchess.adapter.board.command.in.websocket.dto.MovePieceRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.ProposeDrawRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.ProposeTakingBackMoveRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.RejectDrawRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.RejectTakingBackMoveRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.ResignGameRequest;
import pl.illchess.domain.board.event.BoardPiecesLocationsUpdated;

import java.util.UUID;

@RequestMapping("/api/board")
public interface BoardCommandApi {

    void checkGameState(BoardPiecesLocationsUpdated boardUpdated);

    @ResponseBody
    @PutMapping(value = "/join-or-initialize", produces = "application/json")
    ResponseEntity<InitializedBoardResponse> initializeNewBoard(@RequestBody InitializeNewBoardRequest initializeNewBoardRequest);

    @ResponseBody
    @PutMapping(value = "/move-piece", produces = "application/json")
    ResponseEntity<Void> movePiece(@RequestBody MovePieceRequest movePieceRequest);

    @ResponseBody
    @PutMapping(value = "/legal-moves", produces = "application/json")
    ResponseEntity<LegalMovesResponse> checkLegalityOfMove(@RequestBody CheckLegalMovesRequest request);

    @ResponseBody
    @PutMapping(value = "/resign", produces = "application/json")
    ResponseEntity<Void> resignGame(@RequestBody ResignGameRequest request);

    @ResponseBody
    @PutMapping(value = "/propose-draw", produces = "application/json")
    ResponseEntity<Void> proposeDraw(@RequestBody ProposeDrawRequest request);

    @ResponseBody
    @PutMapping(value = "/reject-draw", produces = "application/json")
    ResponseEntity<Void> rejectDraw(@RequestBody RejectDrawRequest request);

    @ResponseBody
    @PutMapping(value = "/accept-draw", produces = "application/json")
    ResponseEntity<Void> acceptDraw(@RequestBody AcceptDrawRequest request);

    @ResponseBody
    @GetMapping(value = "/fen/{boardId}", produces = "application/json")
    ResponseEntity<BoardFenStringResponse> establishFenString(@PathVariable UUID boardId);

    @ResponseBody
    @PutMapping(value = "/propose-take-back-move", produces = "application/json")
    ResponseEntity<Void> proposeTakingBackMove(@RequestBody ProposeTakingBackMoveRequest request);

    @ResponseBody
    @PutMapping(value = "/accept-take-back-move", produces = "application/json")
    ResponseEntity<Void> acceptTakingBackMove(@RequestBody AcceptTakingBackMoveRequest request);

    @ResponseBody
    @PutMapping(value = "/reject-take-back-move", produces = "application/json")
    ResponseEntity<Void> rejectTakingBackMove(@RequestBody RejectTakingBackMoveRequest request);

}
