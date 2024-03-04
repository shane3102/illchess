package pl.illchess.adapter.board.command.in.websocket.dto;

import pl.illchess.application.board.command.in.InitializeNewBoardUseCase;

import java.util.UUID;

public record InitializeNewBoardRequest(UUID newBoardId) {

    public InitializeNewBoardUseCase.InitializeNewBoardCmd toCmd() {
        return new InitializeNewBoardUseCase.InitializeNewBoardCmd(newBoardId);
    }
}
