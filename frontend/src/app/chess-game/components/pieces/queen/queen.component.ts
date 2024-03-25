import { Component, Input, OnInit } from '@angular/core';
import { PieceColor, getColorByPieceColor } from 'src/app/chess-game/model/PieceInfo';
import { faChessQueen } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-queen',
  templateUrl: './queen.component.html',
  styleUrls: [
    './queen.component.scss',
    '../../chess-board-style.scss'
  ]
})
export class QueenComponent implements OnInit {

  getColorByPieceColor = getColorByPieceColor

  queenIcon = faChessQueen;

  @Input() color: PieceColor;

  constructor() { }

  ngOnInit(): void {
  }

}
