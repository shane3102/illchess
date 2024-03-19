package pl.illchess.adapter.board.command.in.websocket.dto;

import pl.illchess.application.board.command.in.JoinOrInitializeNewGameUseCase;

public record InitializeNewBoardRequest(String username) {

    public JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd toCmd() {
        return new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd(
            username,
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w"
        );
    }
}
