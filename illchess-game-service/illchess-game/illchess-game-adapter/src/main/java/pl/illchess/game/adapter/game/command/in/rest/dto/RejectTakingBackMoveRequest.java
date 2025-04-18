package pl.illchess.game.adapter.game.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.RejectTakingBackLastMoveUseCase.RejectTakingBackMoveCmd;

import java.util.UUID;

public record RejectTakingBackMoveRequest(
    UUID gameId,
    String username
) {
    public RejectTakingBackMoveCmd toCmd() {
        return new RejectTakingBackMoveCmd(
            gameId,
            username
        );
    }
}
