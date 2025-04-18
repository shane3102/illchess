package pl.illchess.game.adapter.board.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.ProposeDrawUseCase;

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
