package pl.illchess.game.adapter.game.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.RejectDrawUseCase;

import java.util.UUID;

public record RejectDrawRequest(
    UUID gameId,
    String username
) {
    public RejectDrawUseCase.RejectDrawCmd toCmd() {
        return new RejectDrawUseCase.RejectDrawCmd(gameId, username);
    }
}
