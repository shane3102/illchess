package pl.illchess.game.adapter.game.command.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.illchess.game.adapter.game.command.in.rest.dto.AcceptDrawRequest;
import pl.illchess.game.adapter.game.command.in.rest.dto.AcceptTakingBackMoveRequest;
import pl.illchess.game.adapter.game.command.in.rest.dto.BoardFenStringResponse;
import pl.illchess.game.adapter.game.command.in.rest.dto.CheckLegalMovesRequest;
import pl.illchess.game.adapter.game.command.in.rest.dto.InitializeNewGameRequest;
import pl.illchess.game.adapter.game.command.in.rest.dto.InitializedGameResponse;
import pl.illchess.game.adapter.game.command.in.rest.dto.LegalMovesResponse;
import pl.illchess.game.adapter.game.command.in.rest.dto.MovePieceRequest;
import pl.illchess.game.adapter.game.command.in.rest.dto.ProposeDrawRequest;
import pl.illchess.game.adapter.game.command.in.rest.dto.ProposeTakingBackMoveRequest;
import pl.illchess.game.adapter.game.command.in.rest.dto.QuitOccupiedGameRequest;
import pl.illchess.game.adapter.game.command.in.rest.dto.RejectDrawRequest;
import pl.illchess.game.adapter.game.command.in.rest.dto.RejectTakingBackMoveRequest;
import pl.illchess.game.adapter.game.command.in.rest.dto.ResignGameRequest;

import java.util.UUID;

@RequestMapping("/api/game")
public interface GameCommandApi {

    @ResponseBody
    @PutMapping(value = "/join-or-initialize", produces = "application/json")
    ResponseEntity<InitializedGameResponse> initializeNewGame(@RequestBody InitializeNewGameRequest initializeNewGameRequest);

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
    @GetMapping(value = "/fen/{gameId}", produces = "application/json")
    ResponseEntity<BoardFenStringResponse> establishFenString(@PathVariable UUID gameId);

    @ResponseBody
    @PutMapping(value = "/propose-take-back-move", produces = "application/json")
    ResponseEntity<Void> proposeTakingBackMove(@RequestBody ProposeTakingBackMoveRequest request);

    @ResponseBody
    @PutMapping(value = "/accept-take-back-move", produces = "application/json")
    ResponseEntity<Void> acceptTakingBackMove(@RequestBody AcceptTakingBackMoveRequest request);

    @ResponseBody
    @PutMapping(value = "/reject-take-back-move", produces = "application/json")
    ResponseEntity<Void> rejectTakingBackMove(@RequestBody RejectTakingBackMoveRequest request);

    @ResponseBody
    @PutMapping(value = "/quit-not-yet-started", produces = "application/json")
    ResponseEntity<Void> quitNotYetStartedGame(@RequestBody QuitOccupiedGameRequest request);

}
