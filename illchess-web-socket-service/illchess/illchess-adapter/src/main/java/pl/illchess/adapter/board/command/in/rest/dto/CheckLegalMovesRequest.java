package pl.illchess.adapter.board.command.in.rest.dto;

import pl.illchess.application.board.command.in.CheckLegalityMoveUseCase;

import java.util.UUID;

public record CheckLegalMovesRequest(
    UUID boardId,
    String startSquare,
    String pieceColor
) {
    public CheckLegalityMoveUseCase.MovePieceAttemptCmd toCmd() {
        return new CheckLegalityMoveUseCase.MovePieceAttemptCmd(
            boardId,
            startSquare,
            pieceColor
        );
    }
}
