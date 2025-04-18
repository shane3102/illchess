package pl.illchess.game.adapter.game.command.in.rest.dto;

import pl.illchess.game.application.game.command.in.MovePieceUseCase;

import java.util.UUID;

public record MovePieceRequest(
    UUID gameId,
    String startSquare,
    String targetSquare,
    String pawnPromotedToPieceType,
    String username
) {
    public MovePieceUseCase.MovePieceCmd toCmd() {
        return new MovePieceUseCase.MovePieceCmd(
            gameId,
            startSquare,
            targetSquare,
            pawnPromotedToPieceType,
            username
        );
    }
}
