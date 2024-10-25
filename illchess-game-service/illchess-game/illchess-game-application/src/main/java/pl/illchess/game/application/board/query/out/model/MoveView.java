package pl.illchess.game.application.board.query.out.model;

public record MoveView(
    String startSquare,
    String targetSquare
) {
}
