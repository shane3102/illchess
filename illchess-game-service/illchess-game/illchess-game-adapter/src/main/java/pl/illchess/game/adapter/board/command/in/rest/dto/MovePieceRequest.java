package pl.illchess.game.adapter.board.command.in.rest.dto;

import pl.illchess.game.application.board.command.in.MovePieceUseCase;

import java.util.UUID;

public record MovePieceRequest(
    UUID boardId,
    String startSquare,
    String targetSquare,
    String pawnPromotedToPieceType,
    String username
) {
    public MovePieceUseCase.MovePieceCmd toCmd() {
        return new MovePieceUseCase.MovePieceCmd(
            boardId,
            startSquare,
            targetSquare,
            pawnPromotedToPieceType,
            username
        );
    }
}
