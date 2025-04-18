package pl.illchess.game.adapter.game.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.AcceptDrawUseCase;

import java.util.UUID;

public record AcceptDrawRequest(
    UUID gameId,
    String username
) {
    public AcceptDrawUseCase.AcceptDrawCmd toCmd() {
        return new AcceptDrawUseCase.AcceptDrawCmd(
            gameId,
            username
        );
    }
}
