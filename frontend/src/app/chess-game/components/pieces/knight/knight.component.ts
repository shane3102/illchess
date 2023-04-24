import { Component, Input, OnInit } from '@angular/core';
import { PieceColor, getColorByPieceColor } from 'src/app/chess-game/model/PieceInfo';
import { faChessKnight } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-knight',
  templateUrl: './knight.component.html',
  styleUrls: ['./knight.component.scss']
})
export class KnightComponent implements OnInit {

  getColorByPieceColor = getColorByPieceColor
  
  knightIcon = faChessKnight;

  @Input() color: PieceColor;

  constructor() { }

  ngOnInit(): void {
  }

}
