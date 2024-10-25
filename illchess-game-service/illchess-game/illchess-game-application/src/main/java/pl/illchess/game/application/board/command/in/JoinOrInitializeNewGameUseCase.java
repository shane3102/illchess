package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.board.command.JoinOrInitializeNewGame;
import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.FenBoardString;
import pl.illchess.game.domain.board.model.state.player.Username;

public interface JoinOrInitializeNewGameUseCase {

    BoardId joinOrInitializeNewGame(JoinOrInitializeNewGameCmd joinOrInitializeNewGameCmd);

    record JoinOrInitializeNewGameCmd(
        String username,
        String fenString
    ) {

        public JoinOrInitializeNewGame toCommand() {
            return new JoinOrInitializeNewGame(new Username(username), FenBoardString.fromString(fenString));
        }
    }
}
