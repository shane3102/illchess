import { Component, Input, OnInit } from '@angular/core';
import { PieceColor, getColorByPieceColor } from 'src/app/shared/model/PieceInfo';
import { faChessRook } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-rook',
  templateUrl: './rook.component.html',
  styleUrls: [
    './rook.component.scss',
    '../../chess-board-style.scss'
  ]
})
export class RookComponent implements OnInit {

  getColorByPieceColor = getColorByPieceColor

  rookIcon = faChessRook;

  @Input() color: PieceColor;

  constructor() { }

  ngOnInit(): void {
  }

}
