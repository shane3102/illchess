package pl.illchess.adapter.board.command.in.rest.dto;

import pl.illchess.application.board.command.in.RejectDrawUseCase;

import java.util.UUID;

public record RejectDrawRequest(
    UUID boardId,
    String username
) {
    public RejectDrawUseCase.RejectDrawCmd toCmd() {
        return new RejectDrawUseCase.RejectDrawCmd(boardId, username);
    }
}
