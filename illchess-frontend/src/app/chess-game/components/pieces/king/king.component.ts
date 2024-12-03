import { Component, Input, OnInit } from '@angular/core';
import { PieceColor, getColorByPieceColor } from 'src/app/shared/model/game/PieceInfo';
import { faChessKing } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-king',
  templateUrl: './king.component.html',
  styleUrls: [
    './king.component.scss',
    '../../chess-board-style.scss'
  ]
})
export class KingComponent {

  getColorByPieceColor = getColorByPieceColor

  kingIcon = faChessKing;

  @Input() color: PieceColor;

}
