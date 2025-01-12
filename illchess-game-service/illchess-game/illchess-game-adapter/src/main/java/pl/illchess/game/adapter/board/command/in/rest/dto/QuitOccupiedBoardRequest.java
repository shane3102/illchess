package pl.illchess.game.adapter.board.command.in.rest.dto;

import java.util.UUID;
import pl.illchess.game.application.board.command.in.QuitOccupiedBoardUseCase.QuitOccupiedBoardCmd;

public record QuitOccupiedBoardRequest(
    UUID boardId,
    String username
) {
    public QuitOccupiedBoardCmd toCmd() {
        return new QuitOccupiedBoardCmd(
            boardId, username
        );
    }
}
