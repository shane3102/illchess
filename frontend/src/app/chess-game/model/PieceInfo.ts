export class PieceInfo {
    type: Piece;
    color: PieceColor;
}

export enum Piece {
    PAWN = "PAWN", 
    KNIGHT = "KNIGHT", 
    BISHOP = "BISHOP", 
    ROOK = "ROOK", 
    QUEEN = "QUEEN", 
    KING = "KING"
}

export enum PieceColor {
    WHITE = "WHITE", 
    BLACK = "BLACK"
}

export function getColorByPieceColor(color: PieceColor): string {
    return color == PieceColor.WHITE ? 'lightgray' : 'black';
}