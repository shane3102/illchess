package pl.illchess.game.domain.piece.model.info;

public final class CurrentPlayerColor {
    private PieceColor color;

    public CurrentPlayerColor(PieceColor color) {
        this.color = color;
    }

    public PieceColor color() {
        return color;
    }

    public void invert() {
        this.color = color.inverted();
    }
}
