import { Component, Input, OnInit } from '@angular/core';
import { PieceColor } from 'src/app/chess-game/model/PieceInfo';

@Component({
  selector: 'app-queen',
  templateUrl: './queen.component.html',
  styleUrls: ['./queen.component.scss']
})
export class QueenComponent implements OnInit {

  @Input() color: PieceColor;

  constructor() { }

  ngOnInit(): void {
  }

}
