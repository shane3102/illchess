import { Square } from "./BoardView"
import { Piece, PieceColor} from "./PieceInfo"

export interface MovePieceRequest {
    boardId: string
    startSquare: string
    targetSquare: string
    pieceColor: PieceColor
    pieceType: Piece
    pawnPromotedToPieceType?: Piece.QUEEN | Piece.KNIGHT | Piece.ROOK | Piece.BISHOP,
    username: string
}