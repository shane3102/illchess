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

public record PieceType(String text) {

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

    public static Class<? extends Piece> getPieceType(PieceType type) {
        return switch (type.text()) {
            case "KING" -> King.class;
            case "QUEEN" -> Queen.class;
            case "KNIGHT" -> Knight.class;
            case "ROOK" -> Rook.class;
            case "BISHOP" -> Bishop.class;
            case "PAWN" -> Pawn.class;
            default -> throw new PieceTypeNotRecognisedException(type);
        };
    }
}
