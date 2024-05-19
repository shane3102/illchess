package pl.illchess.domain.board.model;

public record FenBoardString(
        String position,
        String activeColor,
        String castlingAvailability,
        String enPassantTargetSquare,
        String halfMoveClock,
        String fullMoveNumber
) {
}
