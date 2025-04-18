package pl.illchess.game.adapter.game.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.AcceptTakingBackLastMoveUseCase.AcceptTakingBackMoveCmd;

import java.util.UUID;

public record AcceptTakingBackMoveRequest(
    UUID gameId,
    String username
) {
    public AcceptTakingBackMoveCmd toCmd() {
        return new AcceptTakingBackMoveCmd(
            gameId,
            username
        );
    }
}
