import { Component, Input, OnInit } from '@angular/core';
import { Piece, PieceColor, getColorByPieceColor } from '../../../shared/model/PieceInfo';
import { faChessBishop, faChessKing, faChessKnight, faChessPawn, faChessQueen, faChessRook } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-captured-pieces',
  templateUrl: './captured-pieces.component.html',
  styleUrls: ['./captured-pieces.component.scss']
})
export class CapturedPiecesComponent {

  @Input() color: PieceColor
  @Input() capturedPieces: string[]

  Piece = Piece

  getColorByPieceColor = getColorByPieceColor

  bishopIcon = faChessBishop
  kingIcon = faChessKing;
  knightIcon = faChessKnight;
  pawnIcon = faChessPawn;
  queenIcon = faChessQueen;
  rookIcon = faChessRook;

}
