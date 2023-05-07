import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Piece, PieceColor, PieceInfo } from '../../model/PieceInfo';
import { PieceDraggedInfo } from '../../model/PieceDraggedInfo';
import { SquareInfo } from '../../model/SquareInfo';
import { PieceDroppedInfo } from '../../model/PieceDroppedInfo';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-chess-square',
  templateUrl: './chess-square.component.html',
  styleUrls: ['./chess-square.component.scss']
})
export class ChessSquareComponent implements OnInit {

  piece: PieceInfo | undefined;
  @Input() squareInfo: SquareInfo;
  @Input() moveSubject: Observable<PieceDroppedInfo>

  @Output() pieceDraggedInfoEmitter: EventEmitter<PieceDraggedInfo> = new EventEmitter();
  @Output() pieceDroppedInfoEmitter: EventEmitter<SquareInfo> = new EventEmitter();

  isDraggedOver: boolean = false;

  constructor() { }

  ngOnInit(): void {
    this.moveSubject.subscribe(move => {
      if (this.squareInfo == move.squareFromInfo) {
        this.piece = undefined;
        console.log("DZIAŁAJ: " + JSON.stringify(this.piece) + " " + JSON.stringify(this.squareInfo))

      }

      if (this.squareInfo == move.squareToInfo) {
        this.piece = move.pieceInfo;
        console.log("DZIAŁAJ: " + JSON.stringify(this.piece) + " " + JSON.stringify(this.squareInfo))
      }
    });

    this.piece = this.getPieceBySquare(this.squareInfo.file, this.squareInfo.rank)
  }

  public calculateSquareColor(isDraggedOver: boolean): string {

    if (isDraggedOver) {
      return 'green';
    }

    return (this.squareInfo.rank + this.fileToNumber()) % 2 == 0 ? 'brown' : '#F3E5AB';
  }

  private fileToNumber(): number {
    switch (this.squareInfo.file) {
      case 'A':
        return 1;
      case 'B':
        return 2;
      case 'C':
        return 3;
      case 'D':
        return 4;
      case 'E':
        return 5;
      case 'F':
        return 6;
      case 'G':
        return 7;
      case 'H':
        return 8;
    }
    return 0;
  }

  pieceDragged() {
    console.log("dragged")
    this.pieceDraggedInfoEmitter.emit(new PieceDraggedInfo(<PieceInfo>this.piece, this.squareInfo))
  }

  pieceDropped() {
    this.isDraggedOver = false
    console.log("dropped")
    this.pieceDroppedInfoEmitter.emit(this.squareInfo)
  }

  pieceDraggedOver(event: any) {
    event.preventDefault()

    this.isDraggedOver = true;

  }

  getPieceBySquare(file: string, rank: number): PieceInfo | undefined {

    console.log("siema")

    let piece: PieceInfo = new PieceInfo()

    if (rank > 2 && rank < 7) {
      return undefined;
    }
    if (rank == 1 || rank == 2) {
      piece.color = PieceColor.WHITE
    }
    if (rank == 8 || rank == 7) {
      piece.color = PieceColor.BLACK
    }


    if (file == 'A' || file == 'H') {
      piece.piece = Piece.ROOK
    }

    if (file == 'B' || file == 'G') {
      piece.piece = Piece.KNIGHT
    }

    if (file == 'C' || file == 'F') {
      piece.piece = Piece.BISHOP
    }

    if (file == 'E') {
      piece.piece = Piece.KING
    }

    if (file == 'D') {
      piece.piece = Piece.QUEEN
    }

    if (rank == 7 || rank == 2) {
      piece.piece = Piece.PAWN
    }

    return piece;
  }

}
