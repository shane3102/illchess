import { Component, Input, OnInit } from '@angular/core';
import { PieceColor, getColorByPieceColor } from 'src/app/shared/model/game/PieceInfo';
import { faChessKnight } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-knight',
  templateUrl: './knight.component.html',
  styleUrls: [
    './knight.component.scss',
    '../../chess-board-style.scss'
  ]
})
export class KnightComponent {

  getColorByPieceColor = getColorByPieceColor
  
  knightIcon = faChessKnight;

  @Input() color: PieceColor;

}
