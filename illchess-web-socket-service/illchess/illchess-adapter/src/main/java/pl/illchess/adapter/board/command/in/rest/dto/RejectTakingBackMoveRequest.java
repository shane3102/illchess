package pl.illchess.adapter.board.command.in.rest.dto;

import pl.illchess.application.board.command.in.RejectTakingBackLastMoveUseCase.RejectTakingBackMoveCmd;

import java.util.UUID;

public record RejectTakingBackMoveRequest(
    UUID boardId,
    String username
) {
    public RejectTakingBackMoveCmd toCmd() {
        return new RejectTakingBackMoveCmd(
            boardId,
            username
        );
    }
}
