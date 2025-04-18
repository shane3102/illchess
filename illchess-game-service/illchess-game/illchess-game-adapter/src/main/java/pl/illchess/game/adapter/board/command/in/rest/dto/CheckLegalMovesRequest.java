package pl.illchess.game.adapter.board.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.CheckLegalityMoveUseCase;

import java.util.UUID;

public record CheckLegalMovesRequest(
    UUID boardId,
    String startSquare,
    String pieceColor,
    String username
) {
    public CheckLegalityMoveUseCase.MovePieceAttemptCmd toCmd() {
        return new CheckLegalityMoveUseCase.MovePieceAttemptCmd(
            boardId,
            startSquare,
            pieceColor,
            username
        );
    }
}
