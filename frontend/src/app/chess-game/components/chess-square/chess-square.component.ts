import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PieceInfo } from '../../model/PieceInfo';
import { PieceDraggedInfo } from '../../model/PieceDraggedInfo';

@Component({
  selector: 'app-chess-square',
  templateUrl: './chess-square.component.html',
  styleUrls: ['./chess-square.component.scss']
})
export class ChessSquareComponent implements OnInit {

  @Input() rank: number;
  @Input() file: string;
  @Input() piece: PieceInfo | undefined;

  @Output() pieceDraggedInfoEmitter: EventEmitter<PieceDraggedInfo> = new EventEmitter();

  isDragged: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

  public calculateSquareColor(isDragged: boolean): string {

    if(isDragged) {
      return 'green';
    }

    return (this.rank + this.fileToNumber()) % 2 == 0 ? 'brown' : '#F3E5AB';
  }

  private fileToNumber(): number {
    switch (this.file) {
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
    this.pieceDraggedInfoEmitter.emit(new PieceDraggedInfo(<PieceInfo>this.piece, this.rank, this.file))
  }

  pieceDropped() {
    this.isDragged = false
    console.log("dropped")
  }

  pieceDraggedOver(event: any) {
    event.preventDefault()

    this.isDragged = true;

  }

}
