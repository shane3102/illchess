import { Component, Input, OnInit } from '@angular/core';
import { Piece, PieceInfo } from '../../../shared/model/game/PieceInfo';

@Component({
  selector: 'app-chess-piece',
  templateUrl: './chess-piece.component.html',
  styleUrls: [
    './chess-piece.component.scss',
    '../chess-board-style.scss'
  ]
})
export class ChessPieceComponent {

  Piece = Piece

  @Input() pieceInfo: PieceInfo | undefined;

}
