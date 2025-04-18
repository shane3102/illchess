package pl.illchess.game.adapter.board.command.out.redis.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record GameEntity(
    UUID gameId,
    List<PieceEntity> piecesLocations,
    List<MoveEntity> moveStackData,
    GameInfoEntity boardState
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

    public record GameInfoEntity(
        String currentPlayerColor,
        PlayerEntity whitePlayer,
        PlayerEntity blackPlayer,
        String gameState,
        String gameResultCause,
        LocalDateTime startTime
    ) implements Serializable {

    }

    public record PlayerEntity(
        String username,
        boolean isProposingDraw,
        boolean isProposingTakingBackMove,
        List<PreMoveEntity> preMoves
    ) implements Serializable {

    }

    public record PreMoveEntity(
        String startSquare,
        String targetSquare,
        String pawnPromotedToPieceType,
        List<PieceEntity> piecesLocationsAfterPreMove
    ) implements Serializable {

    }
}
