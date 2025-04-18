package pl.illchess.game.adapter.game.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.JoinOrInitializeNewGameUseCase;

public record InitializeNewGameRequest(String username) {

    public JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd toCmd() {
        return new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd(
            username,
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w"
        );
    }
}
