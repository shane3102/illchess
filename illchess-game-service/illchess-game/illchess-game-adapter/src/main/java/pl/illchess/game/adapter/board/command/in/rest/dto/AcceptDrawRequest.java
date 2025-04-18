package pl.illchess.game.adapter.board.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.AcceptDrawUseCase;

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
