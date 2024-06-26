package pl.illchess.adapter.board.command.in.event;

import org.springframework.context.event.EventListener;
import pl.illchess.domain.board.event.BoardPiecesLocationsUpdated;

public interface BoardEventListener {

    @EventListener(BoardPiecesLocationsUpdated.class)
    void checkGameState(BoardPiecesLocationsUpdated boardUpdated);

}
