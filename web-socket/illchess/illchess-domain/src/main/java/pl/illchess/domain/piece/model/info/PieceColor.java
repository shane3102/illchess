package pl.illchess.domain.piece.model.info;

public enum PieceColor {
    WHITE, BLACK;

    public PieceColor invert() {
        return this == WHITE ? BLACK : WHITE;
    }
}
