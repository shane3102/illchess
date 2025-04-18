package pl.illchess.game.adapter.game.command.in.rest.dto;

import java.util.UUID;
import pl.illchess.game.application.game.command.in.QuitOccupiedGameUseCase.QuitOccupiedBoardCmd;

public record QuitOccupiedGameRequest(
    UUID gameId,
    String username
) {
    public QuitOccupiedBoardCmd toCmd() {
        return new QuitOccupiedBoardCmd(
            gameId, username
        );
    }
}
