import { Square } from "./BoardView"
import { Piece, PieceColor} from "./PieceInfo"

export interface MovePieceRequest {
    boardId: string
    startSquare: string
    targetSquare: string
    pieceColor: PieceColor
    pieceType: Piece
}