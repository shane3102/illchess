export class PieceInfo {
    piece: Piece;
    color: PieceColor;
}

export enum Piece {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING
}

export enum PieceColor {
    WHITE, BLACK
}