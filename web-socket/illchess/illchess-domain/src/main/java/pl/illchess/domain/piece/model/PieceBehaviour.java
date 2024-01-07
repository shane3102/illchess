package pl.illchess.domain.piece.model;

import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.exception.PieceTypeNotRecognisedException;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;
import pl.illchess.domain.piece.model.type.Bishop;
import pl.illchess.domain.piece.model.type.King;
import pl.illchess.domain.piece.model.type.Knight;
import pl.illchess.domain.piece.model.type.Pawn;
import pl.illchess.domain.piece.model.type.Queen;
import pl.illchess.domain.piece.model.type.Rook;

import java.util.Objects;
import java.util.Set;

public abstract class PieceBehaviour {

    abstract public PieceColor color();

    abstract public Square square();

    abstract public Set<Square> possibleMoves(PiecesLocations piecesLocations);

    abstract public PieceType typeName();

    public boolean isDefendingSquare(Square square, PiecesLocations piecesLocations) {
        Set<Square> squares = possibleMoves(piecesLocations);
        return squares.stream().anyMatch(checkedSquare -> Objects.equals(checkedSquare.name(), square.name()));
    }

    abstract public void setSquare(Square square);

    public static PieceBehaviour getPieceByPieceType(PieceType type, PieceColor color, Square currentSquare) {
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

}
