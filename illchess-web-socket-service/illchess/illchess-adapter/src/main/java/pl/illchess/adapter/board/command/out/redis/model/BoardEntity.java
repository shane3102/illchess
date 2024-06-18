package pl.illchess.adapter.board.command.out.redis.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record BoardEntity(
    UUID boardId,
    List<PieceEntity> piecesLocations,
    List<MoveEntity> moveStackData,
    BoardStateEntity boardState
) implements Serializable {

    public record PieceEntity(
        String square,
        String pieceColor,
        String pieceType,
        Set<String> cachedReachableSquares
    ) implements Serializable {
    }

    public record MoveEntity(
        String startSquare,
        String targetSquare,
        PieceEntity movedPiece,
        PieceEntity capturedPiece,
        String fenString
    ) implements Serializable {
    }

    public record BoardStateEntity(
        String currentPlayerColor,
        PlayerEntity player1,
        PlayerEntity player2,
        String gameState,
        String victoriousPlayerColor
    ) implements Serializable {

    }

    public record PlayerEntity(
        String username,
        boolean isProposingDraw
    ) implements Serializable {

    }
}
