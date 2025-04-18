package pl.illchess.game.domain.game.model;

public record FenBoardString(
    String position,
    String activeColor,
    String castlingAvailability,
    String enPassantTargetSquare,
    String halfMoveClock,
    String fullMoveNumber
) {

    public String fullString() {
        return "%s %s %s %s %s %s".formatted(
            position(),
            activeColor(),
            castlingAvailability(),
            enPassantTargetSquare(),
            halfMoveClock(),
            fullMoveNumber()
        );
    }

    public static FenBoardString fromString(String value) {
        String[] valueSplited = value.split(" ");

        return new FenBoardString(
            valueSplited[0],
            valueSplited.length <= 1 ? "w" : valueSplited[1],
            valueSplited.length <= 2 ? "KQkq" : valueSplited[2],
            valueSplited.length <= 3 ? "-" : valueSplited[3],
            valueSplited.length <= 4 ? "0" : valueSplited[4],
            valueSplited.length <= 5 ? "0" : valueSplited[5]
        );
    }
}
