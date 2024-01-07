package pl.illchess.domain.board.exception;


import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.PieceBehaviour;

public class PieceNotPresentOnGivenSquare extends IllegalMoveException {
    public PieceNotPresentOnGivenSquare(
            BoardId boardId,
            PieceBehaviour piece,
            Square square
    ) {
        super(
                "Piece %s not present on %s square".formatted(piece, square),
                boardId,
                square
        );
    }
}
