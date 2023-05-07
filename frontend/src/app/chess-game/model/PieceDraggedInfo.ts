import { PieceInfo } from "./PieceInfo";

export class PieceDraggedInfo {
    pieceInfo: PieceInfo;
    rank: number;
    file: string;

    constructor(pieceInfo: PieceInfo, rank: number, file: string){
        this.pieceInfo = pieceInfo;
        this.rank = rank;
        this.file = file;
    }
}