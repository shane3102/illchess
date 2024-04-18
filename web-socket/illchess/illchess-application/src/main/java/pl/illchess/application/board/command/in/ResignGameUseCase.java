package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.Resign;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.player.Username;

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
