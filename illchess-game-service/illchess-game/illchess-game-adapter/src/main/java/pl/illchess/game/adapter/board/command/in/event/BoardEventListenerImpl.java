package pl.illchess.game.adapter.board.command.in.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.illchess.game.application.game.command.in.CheckGameStateUseCase;
import pl.illchess.game.application.game.command.in.CheckGameStateUseCase.CheckBoardStateCmd;
import pl.illchess.game.domain.game.event.GamePiecesLocationsUpdated;

@Component
@RequiredArgsConstructor
public class BoardEventListenerImpl implements BoardEventListener {

    private final CheckGameStateUseCase checkGameStateUseCase;

    @Override
    public void checkGameState(GamePiecesLocationsUpdated boardUpdated) {
        CheckBoardStateCmd cmd = new CheckBoardStateCmd(boardUpdated.gameId().uuid());
        checkGameStateUseCase.checkBoardState(cmd);
    }

}
