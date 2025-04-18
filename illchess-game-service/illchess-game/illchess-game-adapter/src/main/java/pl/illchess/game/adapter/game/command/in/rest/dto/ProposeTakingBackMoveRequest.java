package pl.illchess.game.adapter.game.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.ProposeTakingBackLastMoveUseCase.ProposeTakingBackMoveCmd;

import java.util.UUID;

public record ProposeTakingBackMoveRequest(
    UUID gameId,
    String username
) {
    public ProposeTakingBackMoveCmd toCmd() {
        return new ProposeTakingBackMoveCmd(
            gameId,
            username
        );
    }
}
