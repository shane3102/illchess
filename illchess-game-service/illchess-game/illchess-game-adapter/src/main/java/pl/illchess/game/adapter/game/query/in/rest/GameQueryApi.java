package pl.illchess.game.adapter.game.query.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.illchess.game.application.game.query.out.model.ActiveGamesView;
import pl.illchess.game.application.game.query.out.model.GameAdditionalInfoView;
import pl.illchess.game.application.game.query.out.model.GameView;
import pl.illchess.game.application.game.query.out.model.GameWithPreMovesView;

import java.util.UUID;

@RequestMapping("/api/game")
public interface GameQueryApi {

    @GetMapping("/refresh/{gameId}")
    ResponseEntity<GameView> refreshBoardView(@PathVariable UUID gameId);

    @GetMapping("/refresh/pre-moves/{gameId}/{username}")
    ResponseEntity<GameWithPreMovesView> refreshBoardWithPreMovesView(@PathVariable UUID gameId, @PathVariable String username);

    @GetMapping("/refresh/info/{gameId}")
    ResponseEntity<GameAdditionalInfoView> refreshBoardInfoView(@PathVariable UUID gameId);

    @GetMapping("/active")
    ResponseEntity<ActiveGamesView> refreshActiveBoardsView();
}
