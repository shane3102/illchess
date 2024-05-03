package pl.illchess.adapter.board.command.in.websocket.dto;

import pl.illchess.application.board.command.in.AcceptDrawUseCase;

import java.util.UUID;

public record AcceptDrawRequest(
    UUID boardId,
    String username
) {
    public AcceptDrawUseCase.AcceptDrawCmd toCmd() {
        return new AcceptDrawUseCase.AcceptDrawCmd(
            boardId,
            username
        );
    }
}
