package pl.illchess.game.adapter.game.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.ProposeDrawUseCase;

import java.util.UUID;

public record ProposeDrawRequest(
    UUID gameId,
    String username
) {
    public ProposeDrawUseCase.ProposeDrawCmd toCmd() {
        return new ProposeDrawUseCase.ProposeDrawCmd(
            gameId,
            username
        );
    }
}
