import { Component, Input, OnInit } from '@angular/core';
import { Piece, PieceColor, PieceInfo } from '../../model/PieceInfo';
import { PieceDraggedInfo } from '../../model/PieceDraggedInfo';
import { SquareInfo } from '../../model/SquareInfo';
import { PieceDroppedInfo } from '../../model/PieceDroppedInfo';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-chess-board',
  templateUrl: './chess-board.component.html',
  styleUrls: ['./chess-board.component.scss']
})
export class ChessBoardComponent implements OnInit {

  moveSubject: Subject<PieceDroppedInfo> = new Subject<PieceDroppedInfo>();

  ranks: number[] = [8, 7, 6, 5, 4, 3, 2, 1]
  files: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

  currentllyDraggedPiece: PieceDraggedInfo;

  constructor() { }

  ngOnInit(): void {
  }

  pieceDraggedChange(pieceDraggedInfo: PieceDraggedInfo){
    this.currentllyDraggedPiece = pieceDraggedInfo;
  }

  pieceDroppedChange(pieceDroppedInfo: SquareInfo){
    this.moveSubject.next({pieceInfo: this.currentllyDraggedPiece.pieceInfo, squareFromInfo: this.currentllyDraggedPiece.squareInfo, squareToInfo: pieceDroppedInfo})
  }

}
