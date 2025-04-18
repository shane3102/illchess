package pl.illchess.game.adapter.game.command.in.event;

import org.springframework.context.event.EventListener;
import pl.illchess.game.domain.game.event.GamePiecesLocationsUpdated;

public interface BoardEventListener {

    @EventListener(GamePiecesLocationsUpdated.class)
    void checkGameState(GamePiecesLocationsUpdated boardUpdated);

}
