package pl.illchess.domain.piece.model.info;

import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.exception.PieceTypeNotRecognisedException;
import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.type.Bishop;
import pl.illchess.domain.piece.model.type.King;
import pl.illchess.domain.piece.model.type.Knight;
import pl.illchess.domain.piece.model.type.Pawn;
import pl.illchess.domain.piece.model.type.Queen;
import pl.illchess.domain.piece.model.type.Rook;

import java.util.Map;

public record PieceType(String text) {

    private static final Map<PieceType, Integer> valueOfPieces = Map.of(
            new PieceType("QUEEN"), 9,
            new PieceType("ROOK"), 5,
            new PieceType("BISHOP"), 3,
            new PieceType("KNIGHT"), 3,
            new PieceType("PAWN"), 1
    );

    public static Piece getPieceByPieceType(PieceType type, PieceColor color, Square currentSquare) {
        return switch (type.text()) {
            case "KING" -> new King(color, currentSquare);
            case "QUEEN" -> new Queen(color, currentSquare);
            case "ROOK" -> new Rook(color, currentSquare);
            case "BISHOP" -> new Bishop(color, currentSquare);
            case "KNIGHT" -> new Knight(color, currentSquare);
            case "PAWN" -> new Pawn(color, currentSquare);
            default -> throw new PieceTypeNotRecognisedException(type);
        };
    }

    public static int pieceTypeComparator(PieceType piece1, PieceType piece2) {
        return valueOfPieces.get(piece2) - valueOfPieces.get(piece1);
    }

    public static int pieceTypeComparator(String piece1, String piece2) {
        return valueOfPieces.get(new PieceType(piece2)) - valueOfPieces.get(new PieceType(piece1));
    }
}
