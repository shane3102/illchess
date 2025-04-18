package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.game.command.ProposeTakingBackMove;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

import java.util.UUID;

public interface ProposeTakingBackLastMoveUseCase {

    void proposeTakingBackMove(ProposeTakingBackMoveCmd cmd);

    record ProposeTakingBackMoveCmd(UUID boardId, String username) {
        public ProposeTakingBackMove toCommand() {
            return new ProposeTakingBackMove(
                new GameId(boardId),
                new Username(username)
            );
        }
    }
}
