package pl.illchess.domain.piece.model.info;

public enum PieceColor {
    WHITE, BLACK;

    public PieceColor inverted() {
        return this == WHITE ? BLACK : WHITE;
    }
}
