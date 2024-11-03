import { Component, Input } from '@angular/core';
import { Piece, PieceInfo, getColorByPieceColor } from '../../../shared/model/PieceInfo';
import { faChessBishop, faChessKing, faChessKnight, faChessPawn, faChessQueen, faChessRook } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-chess-piece-mini',
  templateUrl: './chess-piece-mini.component.html',
  styleUrls: ['./chess-piece-mini.component.scss']
})
export class ChessPieceMiniComponent {

  Piece = Piece

  @Input() pieceInfo: PieceInfo | undefined;

  getColorByPieceColor = getColorByPieceColor

  bishopIcon = faChessBishop
  kingIcon = faChessKing;
  knightIcon = faChessKnight;
  pawnIcon = faChessPawn;
  queenIcon = faChessQueen;
  rookIcon = faChessRook;

}
