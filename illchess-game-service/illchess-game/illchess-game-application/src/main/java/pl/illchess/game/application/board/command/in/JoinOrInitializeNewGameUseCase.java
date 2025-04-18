package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.game.command.JoinOrInitializeNewGame;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.FenBoardString;
import pl.illchess.game.domain.game.model.state.player.Username;

public interface JoinOrInitializeNewGameUseCase {

    GameId joinOrInitializeNewGame(JoinOrInitializeNewGameCmd joinOrInitializeNewGameCmd);

    record JoinOrInitializeNewGameCmd(
        String username,
        String fenString
    ) {

        public JoinOrInitializeNewGame toCommand() {
            return new JoinOrInitializeNewGame(new Username(username), FenBoardString.fromString(fenString));
        }
    }
}
