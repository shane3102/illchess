package pl.illchess.game.application.game.query.out.model;

public record PlayerView(
    String username,
    boolean isProposingDraw,
    boolean isProposingTakingBackMove
) {
}
