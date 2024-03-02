import { Component, Input } from '@angular/core';
import { Piece, PieceColor } from '../../model/PieceInfo';

@Component({
  selector: 'app-chess-promoting-piece',
  templateUrl: './chess-promoting-piece.component.html',
  styleUrls: ['./chess-promoting-piece.component.scss']
})
export class ChessPromotingPieceComponent {
  @Input() rank: 1 | 8

  Piece = Piece
  PieceColor = PieceColor
}
