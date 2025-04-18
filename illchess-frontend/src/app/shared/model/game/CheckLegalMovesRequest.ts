import { PieceColor } from "./PieceInfo";

export interface CheckLegalMovesRequest{
    gameId: string,
    startSquare: string,
    pieceColor: PieceColor,
    username: string
}