import { Component, Input, OnInit } from '@angular/core';
import { PieceColor, getColorByPieceColor } from 'src/app/chess-game/model/PieceInfo';
import { faChessBishop } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-bishop',
  templateUrl: './bishop.component.html',
  styleUrls: ['./bishop.component.scss']
})
export class BishopComponent implements OnInit {

  getColorByPieceColor = getColorByPieceColor

  bishopIcon = faChessBishop

  @Input() color: PieceColor;

  constructor() { }

  ngOnInit(): void {
    getColorByPieceColor(this.color)
  }

}
