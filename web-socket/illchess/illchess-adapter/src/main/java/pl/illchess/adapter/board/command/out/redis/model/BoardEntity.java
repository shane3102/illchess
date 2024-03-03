package pl.illchess.adapter.board.command.out.redis.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record BoardEntity(
        UUID boardId,
        // rewrite to list/set
        List<PieceEntity> piecesLocations,
        List<MoveEntity> moveStackData,
        BoardStateEntity boardState
) implements Serializable {

    public record PieceEntity(
            String square,
            String pieceColor,
            String pieceType
    ) implements Serializable {
    }

    public record MoveEntity(
            String startSquare,
            String targetSquare,
            PieceEntity movedPiece,
            PieceEntity capturedPiece,
            boolean isEnPassant,
            boolean isCastling,
            String promotionPieceType
    ) implements Serializable {
    }

    public record BoardStateEntity(
            String currentPlayerColor,
            String gameState,
            String victoriousPlayerColor
    ) implements Serializable {

    }
}
