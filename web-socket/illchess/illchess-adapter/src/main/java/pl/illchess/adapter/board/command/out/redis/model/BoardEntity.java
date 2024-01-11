package pl.illchess.adapter.board.command.out.redis.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record BoardEntity(
        UUID boardId,
        // rewrite to list/set
        List<PieceEntity> piecesLocations,
        String currentPlayerColor,
        List<MoveEntity> moveStackData
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
            PieceEntity capturedPiece
    ) implements Serializable {
    }
}
