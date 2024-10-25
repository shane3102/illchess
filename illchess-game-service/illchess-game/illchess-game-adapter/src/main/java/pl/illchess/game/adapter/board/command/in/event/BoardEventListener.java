package pl.illchess.game.adapter.board.command.in.event;

import org.springframework.context.event.EventListener;
import pl.illchess.game.domain.board.event.BoardPiecesLocationsUpdated;

public interface BoardEventListener {

    @EventListener(BoardPiecesLocationsUpdated.class)
    void checkGameState(BoardPiecesLocationsUpdated boardUpdated);

}
