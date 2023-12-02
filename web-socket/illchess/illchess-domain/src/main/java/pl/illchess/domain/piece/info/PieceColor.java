package pl.illchess.domain.piece.info;

public enum PieceColor {
    WHITE, BLACK;

    public PieceColor invert() {
        return this == WHITE ? BLACK : WHITE;
    }
}
