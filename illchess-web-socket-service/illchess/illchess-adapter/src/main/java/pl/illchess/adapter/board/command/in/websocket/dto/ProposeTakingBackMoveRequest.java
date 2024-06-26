package pl.illchess.adapter.board.command.in.websocket.dto;

import pl.illchess.application.board.command.in.ProposeTakeBackMoveUseCase.ProposeTakingBackMoveCmd;

import java.util.UUID;

public record ProposeTakingBackMoveRequest(
    UUID boardId,
    String username
) {
    public ProposeTakingBackMoveCmd toCmd() {
        return new ProposeTakingBackMoveCmd(
            boardId,
            username
        );
    }
}
