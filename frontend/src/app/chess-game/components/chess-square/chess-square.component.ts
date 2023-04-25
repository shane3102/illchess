import { Component, Input, OnInit } from '@angular/core';
import { PieceInfo } from '../../model/PieceInfo';

@Component({
  selector: 'app-chess-square',
  templateUrl: './chess-square.component.html',
  styleUrls: ['./chess-square.component.scss']
})
export class ChessSquareComponent implements OnInit {

  @Input() rank: number;
  @Input() file: string;
  @Input() piece: PieceInfo | undefined;

  constructor() { }

  ngOnInit(): void {
  }

  public calculateSquareColor(): string {
    return (this.rank + this.fileToNumber()) % 2 == 0 ? '#F3E5AB' : 'brown';
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
