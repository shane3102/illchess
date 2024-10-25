package pl.illchess.game.domain.board.exception;


import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.square.Square;
import pl.illchess.game.domain.piece.model.Piece;

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

    public PieceNotPresentOnGivenSquare(
        BoardId boardId,
        Square square
    ) {
        super(
            "Piece not present on %s square".formatted(square),
            boardId,
            square
        );
    }
}
