package pl.illchess.game.adapter.board.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.AcceptTakingBackLastMoveUseCase.AcceptTakingBackMoveCmd;

import java.util.UUID;

public record AcceptTakingBackMoveRequest(
    UUID boardId,
    String username
) {
    public AcceptTakingBackMoveCmd toCmd() {
        return new AcceptTakingBackMoveCmd(
            boardId,
            username
        );
    }
}
