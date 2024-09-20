import { Piece } from "./PieceInfo"

export interface MovePieceRequest {
    boardId: string
    startSquare: string
    targetSquare: string
    pawnPromotedToPieceType?: Piece.QUEEN | Piece.KNIGHT | Piece.ROOK | Piece.BISHOP,
    username: string
}