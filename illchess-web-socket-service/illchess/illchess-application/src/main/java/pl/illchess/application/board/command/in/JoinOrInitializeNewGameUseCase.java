package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.JoinOrInitializeNewGame;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.FenString;
import pl.illchess.domain.board.model.state.player.Username;

public interface JoinOrInitializeNewGameUseCase {

    BoardId joinOrInitializeNewGame(JoinOrInitializeNewGameCmd joinOrInitializeNewGameCmd);

    record JoinOrInitializeNewGameCmd(
        String username,
        String fenString
    ) {

        public JoinOrInitializeNewGame toCommand() {
            return new JoinOrInitializeNewGame(new Username(username), new FenString(fenString));
        }
    }
}
