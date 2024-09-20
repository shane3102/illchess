import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MovePieceRequest } from '../../model/MovePieceRequest';
import { PieceDraggedInfo } from '../../model/PieceDraggedInfo';
import { Piece, PieceColor } from '../../model/PieceInfo';
import { SquareInfo } from '../../model/SquareInfo';

@Component({
  selector: 'app-chess-promoting-piece',
  templateUrl: './chess-promoting-piece.component.html',
  styleUrls: ['./chess-promoting-piece.component.scss']
})
export class ChessPromotingPieceComponent {
  @Input() boardId: string;
  @Input() squareInfo: SquareInfo;
  @Input() draggedPieceInfo: PieceDraggedInfo | undefined | null;
  @Input() username: string

  @Output() pieceDroppedInfoEmitter: EventEmitter<MovePieceRequest> = new EventEmitter();

  Piece = Piece
  PieceColor = PieceColor

  piecePromotion(piece: Piece.QUEEN | Piece.KNIGHT | Piece.ROOK | Piece.BISHOP) {
    if (this.draggedPieceInfo) {
      let moveRequest: MovePieceRequest = {
        'boardId': this.boardId,
        'startSquare': this.draggedPieceInfo.squareInfo.file + this.draggedPieceInfo.squareInfo.rank,
        'targetSquare': this.squareInfo.file + this.squareInfo.rank,
        'pawnPromotedToPieceType': piece,
        'username': this.username
      }
      this.pieceDroppedInfoEmitter.emit(moveRequest)
    }
  }
}
