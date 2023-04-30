import { Component, Input, OnInit } from '@angular/core';
import { PieceInfo } from '../../model/PieceInfo';
import { CdkDragDrop, transferArrayItem } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-chess-square',
  templateUrl: './chess-square.component.html',
  styleUrls: ['./chess-square.component.scss']
})
export class ChessSquareComponent implements OnInit {

  @Input() rank: number;
  @Input() file: string;
  @Input() piece: PieceInfo[];

  constructor() { }

  ngOnInit(): void {
  }

  public calculateSquareColor(): string {
    return (this.rank + this.fileToNumber()) % 2 == 0 ? 'brown' : '#F3E5AB';
  }

  piecePut(event: CdkDragDrop<PieceInfo[]>){
    transferArrayItem(
      event.previousContainer.data,
      event.container.data,
      event.previousIndex,
      event.currentIndex
    );
    console.log(event);

    console.log(this.piece)
    console.log(this.file)
    console.log(this.rank)
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

}
