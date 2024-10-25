package pl.illchess.game.adapter.board.command.in.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.illchess.game.application.board.command.in.CheckIfCheckmateOrStalemateUseCase;
import pl.illchess.game.domain.board.event.BoardPiecesLocationsUpdated;

@Component
@RequiredArgsConstructor
public class BoardEventListenerImpl implements BoardEventListener {

    private final CheckIfCheckmateOrStalemateUseCase checkIfCheckmateOrStalemateUseCase;

    @Override
    public void checkGameState(BoardPiecesLocationsUpdated boardUpdated) {
        CheckIfCheckmateOrStalemateUseCase.CheckIsCheckmateOrStaleMateCmd cmd = new CheckIfCheckmateOrStalemateUseCase.CheckIsCheckmateOrStaleMateCmd(boardUpdated.boardId().uuid());
        checkIfCheckmateOrStalemateUseCase.checkIfCheckmateOrStalemate(cmd);
    }

}
