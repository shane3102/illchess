import { Piece } from "./PieceInfo"

export interface MovePieceRequest {
    gameId: string
    startSquare: string
    targetSquare: string
    pawnPromotedToPieceType?: Piece.QUEEN | Piece.KNIGHT | Piece.ROOK | Piece.BISHOP,
    username: string
}