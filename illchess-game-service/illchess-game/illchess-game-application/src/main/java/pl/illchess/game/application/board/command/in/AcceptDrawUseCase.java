package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.game.command.AcceptDraw;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

import java.util.UUID;

public interface AcceptDrawUseCase {

    void acceptDraw(AcceptDrawCmd cmd);

    record AcceptDrawCmd(
        UUID boardId,
        String username
    ) {
        public AcceptDraw toCommand() {
            return new AcceptDraw(
                new GameId(boardId),
                new Username(username)
            );
        }
    }
}
