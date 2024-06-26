package pl.illchess.adapter.board.command.in.rest.dto;

import pl.illchess.application.board.command.in.AcceptTakingBackLastMoveUseCase.AcceptTakingBackMoveCmd;

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
