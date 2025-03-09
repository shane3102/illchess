import { PieceInfo } from "./PieceInfo";
import { SquareInfo } from "./SquareInfo";

export class PieceSelectedInfo {
    pieceInfo: PieceInfo;
    squareInfo: SquareInfo;

    constructor(pieceInfo: PieceInfo, squareInfo: SquareInfo){
        this.pieceInfo = pieceInfo;
        this.squareInfo = squareInfo
    }
}