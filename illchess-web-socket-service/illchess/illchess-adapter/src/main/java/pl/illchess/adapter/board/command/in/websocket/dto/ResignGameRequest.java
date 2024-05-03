package pl.illchess.adapter.board.command.in.websocket.dto;

import pl.illchess.application.board.command.in.ResignGameUseCase;

import java.util.UUID;

public record ResignGameRequest(
    UUID boardId,
    String username
) {
    public ResignGameUseCase.ResignGameCmd toCmd() {
        return new ResignGameUseCase.ResignGameCmd(boardId, username);
    }
}
