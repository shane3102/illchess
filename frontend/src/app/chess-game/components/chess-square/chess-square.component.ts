import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PieceInfo } from '../../model/PieceInfo';
import { PieceDraggedInfo } from '../../model/PieceDraggedInfo';
import { SquareInfo } from '../../model/SquareInfo';
import { Observable } from 'rxjs';
import { IllegalMoveView } from '../../model/IllegalMoveView';
import { MovePieceRequest } from '../../model/MovePieceRequest';

@Component({
  selector: 'app-chess-square',
  templateUrl: './chess-square.component.html',
  styleUrls: ['./chess-square.component.scss']
})
export class ChessSquareComponent implements OnInit {

  @Input() boardId: string;
  @Input() piece: PieceInfo | undefined;
  @Input() squareInfo: SquareInfo;
  @Input() illegalMoveView: Observable<IllegalMoveView>
  @Input() draggedPieceInfo: PieceDraggedInfo | undefined | null;

  @Output() pieceDraggedInfoEmitter: EventEmitter<PieceDraggedInfo> = new EventEmitter();
  @Output() pieceDroppedInfoEmitter: EventEmitter<MovePieceRequest> = new EventEmitter();

  isDraggedOver: boolean = false;
  illegalMove: boolean = false;

  constructor() { }

  ngOnInit(): void {

    this.illegalMoveView.subscribe(
      illegalMoveView => {
        if (illegalMoveView.highlightSquare == this.squareInfo.file + this.squareInfo.rank) {
          this.displayIllegalMoveAnimation()
        }
      }
    )
  }

  public calculateSquareColor(isDraggedOver: boolean): string {

    if (isDraggedOver) {
      return 'green';
    }

    return (this.squareInfo.rank + this.fileToNumber()) % 2 == 0 ? 'rgba(150, 75, 0)' : '#F3E5AB';
  }

  public displayIllegalMoveAnimation() {
    this.illegalMove = true
    setTimeout(() => this.illegalMove = false, 2000)
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
    this.pieceDraggedInfoEmitter.emit(new PieceDraggedInfo(<PieceInfo>this.piece, this.squareInfo))
  }

  pieceDropped() {
    this.isDraggedOver = false
    if (this.draggedPieceInfo) {
      let moveRequest: MovePieceRequest = {
        'boardId': this.boardId,
        'startSquare': this.draggedPieceInfo.squareInfo.file + this.draggedPieceInfo.squareInfo.rank,
        'targetSquare': this.squareInfo.file + this.squareInfo.rank,
        'pieceColor': this.draggedPieceInfo.pieceInfo.color,
        'pieceType': this.draggedPieceInfo.pieceInfo.type
      }
      this.pieceDroppedInfoEmitter.emit(moveRequest)
    }
  }

  pieceDraggedOver(event: any) {
    event.preventDefault()

    this.isDraggedOver = true;

  }

}
