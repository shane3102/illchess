package pl.illchess.game.application.board.query.out.model;

public record PlayerView(
    String username,
    boolean isProposingDraw,
    boolean isProposingTakingBackMove
) {
}
