import { Component, Input, OnInit } from '@angular/core';
import { PieceColor, getColorByPieceColor } from 'src/app/shared/model/game/PieceInfo';
import { faChessPawn } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-pawn',
  templateUrl: './pawn.component.html',
  styleUrls: [
    './pawn.component.scss',
    '../../chess-board-style.scss'
  ]
})
export class PawnComponent {

  getColorByPieceColor = getColorByPieceColor

  pawnIcon = faChessPawn;

  @Input() color: PieceColor;

}
