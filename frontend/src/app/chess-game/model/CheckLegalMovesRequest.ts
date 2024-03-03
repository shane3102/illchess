import { Piece, PieceColor } from "./PieceInfo";

export interface CheckLegalMovesRequest{
    boardId: string,
    startSquare: string,
    pieceColor: PieceColor,
    pieceType: Piece
}