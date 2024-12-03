import { Component, Input, OnInit } from '@angular/core';
import { PieceColor, getColorByPieceColor } from 'src/app/shared/model/game/PieceInfo';
import { faChessBishop } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-bishop',
  templateUrl: './bishop.component.html',
  styleUrls: [
    './bishop.component.scss',
    '../../chess-board-style.scss'
  ]
})
export class BishopComponent {

  getColorByPieceColor = getColorByPieceColor

  bishopIcon = faChessBishop

  @Input() color: PieceColor;

}
