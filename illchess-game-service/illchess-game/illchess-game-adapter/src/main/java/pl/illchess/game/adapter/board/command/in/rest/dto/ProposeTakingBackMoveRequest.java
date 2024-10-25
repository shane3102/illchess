package pl.illchess.game.adapter.board.command.in.rest.dto;

import pl.illchess.game.application.board.command.in.ProposeTakingBackLastMoveUseCase.ProposeTakingBackMoveCmd;

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
