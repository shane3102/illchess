package pl.illchess.game.adapter.board.command.in.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.illchess.game.application.board.command.in.CheckBoardStateUseCase;
import pl.illchess.game.application.board.command.in.CheckBoardStateUseCase.CheckBoardStateCmd;
import pl.illchess.game.domain.game.event.GamePiecesLocationsUpdated;

@Component
@RequiredArgsConstructor
public class BoardEventListenerImpl implements BoardEventListener {

    private final CheckBoardStateUseCase checkBoardStateUseCase;

    @Override
    public void checkGameState(GamePiecesLocationsUpdated boardUpdated) {
        CheckBoardStateCmd cmd = new CheckBoardStateCmd(boardUpdated.gameId().uuid());
        checkBoardStateUseCase.checkBoardState(cmd);
    }

}
