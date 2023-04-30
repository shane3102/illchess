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

export function getColorByPieceColor(color: PieceColor): string {
    return color == PieceColor.WHITE ? 'lightgray' : 'black';
}