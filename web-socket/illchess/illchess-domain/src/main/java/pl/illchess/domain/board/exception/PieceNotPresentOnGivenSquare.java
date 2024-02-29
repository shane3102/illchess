package pl.illchess.domain.board.exception;


import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.Piece;

public class PieceNotPresentOnGivenSquare extends IllegalMoveException {
    public PieceNotPresentOnGivenSquare(
            BoardId boardId,
            Piece piece,
            Square square
    ) {
        super(
                "Piece %s not present on %s square".formatted(piece, square),
                boardId,
                square
        );
    }

    public PieceNotPresentOnGivenSquare(
        BoardId boardId,
        Class<? extends Piece> expectedPieceType,
        Square square
    ) {
        super(
            "Expected piece %s not present on %s square".formatted(expectedPieceType, square),
            boardId,
            square
        );
    }
}
