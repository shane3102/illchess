package pl.illchess.game.application.game.command.in;

import pl.illchess.game.domain.game.command.ProposeTakingBackMove;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

import java.util.UUID;

public interface ProposeTakingBackLastMoveUseCase {

    void proposeTakingBackMove(ProposeTakingBackMoveCmd cmd);

    record ProposeTakingBackMoveCmd(UUID gameId, String username) {
        public ProposeTakingBackMove toCommand() {
            return new ProposeTakingBackMove(
                new GameId(gameId),
                new Username(username)
            );
        }
    }
}
