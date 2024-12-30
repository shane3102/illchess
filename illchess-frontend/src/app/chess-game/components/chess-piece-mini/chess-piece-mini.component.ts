import { Component, Input } from '@angular/core';
import { Piece, PieceColor, PieceInfo } from '../../../shared/model/game/PieceInfo';

@Component({
  selector: 'app-chess-piece-mini',
  templateUrl: './chess-piece-mini.component.html',
  styleUrls: ['./chess-piece-mini.component.scss']
})
export class ChessPieceMiniComponent {

  Piece = Piece
  PieceColor = PieceColor

  @Input() pieceInfo: PieceInfo | undefined;

}
