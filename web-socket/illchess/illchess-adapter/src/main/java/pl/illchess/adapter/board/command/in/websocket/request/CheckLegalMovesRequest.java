package pl.illchess.adapter.board.command.in.websocket.request;

import pl.illchess.application.board.command.in.CheckLegalityMoveUseCase;

import java.util.UUID;

public record CheckLegalMovesRequest(
    UUID boardId,
    String startSquare,
    String pieceColor,
    String pieceType
) {
    public CheckLegalityMoveUseCase.MovePieceAttemptCmd toCmd() {
        return new CheckLegalityMoveUseCase.MovePieceAttemptCmd(
            boardId,
            startSquare,
            pieceColor,
            pieceType
        );
    }
}
