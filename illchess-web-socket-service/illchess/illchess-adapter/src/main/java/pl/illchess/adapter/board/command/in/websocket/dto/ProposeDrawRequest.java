package pl.illchess.adapter.board.command.in.websocket.dto;

import pl.illchess.application.board.command.in.ProposeDrawUseCase;

import java.util.UUID;

public record ProposeDrawRequest(
    UUID boardId,
    String username
) {
    public ProposeDrawUseCase.ProposeDrawCmd toCmd() {
        return new ProposeDrawUseCase.ProposeDrawCmd(
            boardId,
            username
        );
    }
}
