package pl.illchess.game.adapter.game.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.ResignGameUseCase;

import java.util.UUID;

public record ResignGameRequest(
    UUID gameId,
    String username
) {
    public ResignGameUseCase.ResignGameCmd toCmd() {
        return new ResignGameUseCase.ResignGameCmd(gameId, username);
    }
}
