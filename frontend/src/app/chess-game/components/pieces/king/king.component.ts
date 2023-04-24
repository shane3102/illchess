import { Component, Input, OnInit } from '@angular/core';
import { PieceColor, getColorByPieceColor } from 'src/app/chess-game/model/PieceInfo';
import { faChessKing } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-king',
  templateUrl: './king.component.html',
  styleUrls: ['./king.component.scss']
})
export class KingComponent implements OnInit {

  getColorByPieceColor = getColorByPieceColor

  kingIcon = faChessKing;

  @Input() color: PieceColor;

  constructor() { }

  ngOnInit(): void {
  }

}
