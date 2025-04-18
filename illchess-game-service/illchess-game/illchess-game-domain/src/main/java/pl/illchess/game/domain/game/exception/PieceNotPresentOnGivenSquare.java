package pl.illchess.game.domain.game.exception;


import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.piece.model.Piece;

public class PieceNotPresentOnGivenSquare extends IllegalMoveException {

    public PieceNotPresentOnGivenSquare(
        GameId gameId,
        Class<? extends Piece> expectedPieceType,
        Square square
    ) {
        super(
            "Expected piece %s not present on %s square".formatted(expectedPieceType, square),
            gameId,
            square
        );
    }

    public PieceNotPresentOnGivenSquare(
        GameId gameId,
        Square square
    ) {
        super(
            "Piece not present on %s square".formatted(square),
            gameId,
            square
        );
    }
}
