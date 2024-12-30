import { Component, Input } from '@angular/core';
import { Piece, PieceColor } from '../../../shared/model/game/PieceInfo';

@Component({
  selector: 'app-captured-pieces',
  templateUrl: './captured-pieces.component.html',
  styleUrls: ['./captured-pieces.component.scss']
})
export class CapturedPiecesComponent {

  @Input() color: PieceColor
  @Input() capturedPieces: string[]

  Piece = Piece
  PieceColor = PieceColor

}
