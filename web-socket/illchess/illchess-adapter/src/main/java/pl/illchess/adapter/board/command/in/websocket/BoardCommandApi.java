package pl.illchess.adapter.board.command.in.websocket;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.illchess.adapter.board.command.in.websocket.dto.CheckLegalMovesRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.InitializeNewBoardRequest;
import pl.illchess.adapter.board.command.in.websocket.dto.InitializedBoardResponse;
import pl.illchess.adapter.board.command.in.websocket.dto.LegalMovesResponse;
import pl.illchess.adapter.board.command.in.websocket.dto.MovePieceRequest;
import pl.illchess.domain.board.event.BoardPiecesLocationsUpdated;

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

}
