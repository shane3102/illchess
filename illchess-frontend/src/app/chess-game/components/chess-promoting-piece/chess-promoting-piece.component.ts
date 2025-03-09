import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MovePieceRequest } from '../../../shared/model/game/MovePieceRequest';
import { PieceSelectedInfo } from '../../../shared/model/game/PieceSelectedInfo';
import { Piece, PieceColor } from '../../../shared/model/game/PieceInfo';
import { SquareInfo } from '../../../shared/model/game/SquareInfo';

@Component({
  selector: 'app-chess-promoting-piece',
  templateUrl: './chess-promoting-piece.component.html',
  styleUrls: ['./chess-promoting-piece.component.scss']
})
export class ChessPromotingPieceComponent {
  @Input() boardId: string;
  @Input() squareInfo: SquareInfo;
  @Input() selectedPieceInfo: PieceSelectedInfo | undefined | null;
  @Input() username: string

  @Output() pieceDroppedInfoEmitter: EventEmitter<MovePieceRequest> = new EventEmitter();

  Piece = Piece
  PieceColor = PieceColor

  piecePromotion(piece: Piece.QUEEN | Piece.KNIGHT | Piece.ROOK | Piece.BISHOP) {
    if (this.selectedPieceInfo) {
      let moveRequest: MovePieceRequest = {
        'boardId': this.boardId,
        'startSquare': this.selectedPieceInfo.squareInfo.file + this.selectedPieceInfo.squareInfo.rank,
        'targetSquare': this.squareInfo.file + this.squareInfo.rank,
        'pawnPromotedToPieceType': piece,
        'username': this.username
      }
      this.pieceDroppedInfoEmitter.emit(moveRequest)
    }
  }
}
