package pl.illchess.game.adapter.board.query.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.illchess.game.application.game.query.out.ActiveGamesQueryPort;
import pl.illchess.game.application.game.query.out.GameAdditionalInfoViewQueryPort;
import pl.illchess.game.application.game.query.out.GameViewPreMoveByUserQueryPort;
import pl.illchess.game.application.game.query.out.GameViewQueryPort;
import pl.illchess.game.application.game.query.out.model.ActiveGamesView;
import pl.illchess.game.application.game.query.out.model.GameAdditionalInfoView;
import pl.illchess.game.application.game.query.out.model.GameView;
import pl.illchess.game.application.game.query.out.model.GameWithPreMovesView;
import pl.illchess.game.domain.game.exception.GameNotFoundException;
import pl.illchess.game.domain.game.exception.GameWithPreMovesDoesNotExistException;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BoardQueryController implements BoardQueryApi {

    private final GameViewQueryPort gameViewQueryPort;
    private final GameViewPreMoveByUserQueryPort gameViewPreMoveByUserQueryPort;
    private final ActiveGamesQueryPort activeGamesQueryPort;
    private final GameAdditionalInfoViewQueryPort gameAdditionalInfoViewQueryPort;

    @Override
    public ResponseEntity<GameView> refreshBoardView(UUID boardId) {
        GameView responseView = gameViewQueryPort.findById(boardId).orElseThrow(() -> new GameNotFoundException(boardId));
        return ResponseEntity.ok(responseView);
    }

    @Override
    public ResponseEntity<GameWithPreMovesView> refreshBoardWithPreMovesView(UUID boardId, String username) {
        GameWithPreMovesView responseView = gameViewPreMoveByUserQueryPort.findByIdAndUsername(boardId, username)
            .orElseThrow(() -> new GameWithPreMovesDoesNotExistException(new GameId(boardId), new Username(username)));
        return ResponseEntity.ok(responseView);
    }

    @Override
    public ResponseEntity<GameAdditionalInfoView> refreshBoardInfoView(UUID boardId) {
        GameAdditionalInfoView responseView = gameAdditionalInfoViewQueryPort.findGameById(boardId).orElseThrow(() -> new GameNotFoundException(boardId));
        return ResponseEntity.ok(responseView);
    }

    @Override
    public ResponseEntity<ActiveGamesView> refreshActiveBoardsView() {
        ActiveGamesView response = activeGamesQueryPort.activeGames();
        return ResponseEntity.ok(response);
    }
}
