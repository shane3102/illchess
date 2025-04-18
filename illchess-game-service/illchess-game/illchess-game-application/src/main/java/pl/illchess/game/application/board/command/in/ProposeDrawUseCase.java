package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.game.command.ProposeDraw;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

import java.util.UUID;

public interface ProposeDrawUseCase {

    void proposeDraw(ProposeDrawCmd cmd);

    record ProposeDrawCmd(UUID boardId, String username) {
        public ProposeDraw toCommand() {
            return new ProposeDraw(
                new GameId(boardId),
                new Username(username)
            );
        }
    }
}
