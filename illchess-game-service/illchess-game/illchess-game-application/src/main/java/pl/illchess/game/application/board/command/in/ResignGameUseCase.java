package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.board.command.Resign;
import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.state.player.Username;

import java.util.UUID;

public interface ResignGameUseCase {

    void resignGame(ResignGameCmd cmd);

    record ResignGameCmd(
        UUID boardId,
        String username
    ) {
        public Resign toCommand() {
            return new Resign(
                new BoardId(boardId),
                new Username(username)
            );
        }
    }
}
