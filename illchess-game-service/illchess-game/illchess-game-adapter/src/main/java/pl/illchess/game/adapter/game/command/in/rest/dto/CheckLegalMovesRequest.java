package pl.illchess.game.adapter.game.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.CheckLegalityMoveUseCase;

import java.util.UUID;

public record CheckLegalMovesRequest(
    UUID gameId,
    String startSquare,
    String pieceColor,
    String username
) {
    public CheckLegalityMoveUseCase.MovePieceAttemptCmd toCmd() {
        return new CheckLegalityMoveUseCase.MovePieceAttemptCmd(
            gameId,
            startSquare,
            pieceColor,
            username
        );
    }
}
