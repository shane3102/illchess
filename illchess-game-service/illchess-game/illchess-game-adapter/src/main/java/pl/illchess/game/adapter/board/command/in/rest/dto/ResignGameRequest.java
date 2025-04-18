package pl.illchess.game.adapter.board.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.ResignGameUseCase;

import java.util.UUID;

public record ResignGameRequest(
    UUID boardId,
    String username
) {
    public ResignGameUseCase.ResignGameCmd toCmd() {
        return new ResignGameUseCase.ResignGameCmd(boardId, username);
    }
}
