import { Component, Input } from '@angular/core';
import { Piece, PieceInfo } from '../../../shared/model/game/PieceInfo';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faCrown, faEquals, faHashtag } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-king-board-info-after-game-finished',
  templateUrl: './king-board-info-after-game-finished.component.html',
  styleUrls: ['./king-board-info-after-game-finished.component.scss']
})
export class KingBoardInfoAfterGameFinishedComponent {

  @Input() piece: PieceInfo | undefined;
  @Input() gameState: 'CONTINUE' | 'WHITE_WON' | 'BLACK_WON' | 'DRAW' | null | undefined
  @Input() gameResultCause: 'CHECKMATE' | 'RESIGNATION' | 'STALEMATE' | 'INSUFFICIENT_MATERIAL' | 'PLAYER_AGREEMENT' | null | undefined

  crownIcon = faCrown
  hashtag = faHashtag
  equals = faEquals

  Piece = Piece

  getShowedIcon(): IconDefinition | undefined {
    if (this.piece?.type == Piece.KING) {
      if (this.gameResultCause == 'CHECKMATE' || this.gameResultCause == 'RESIGNATION') {
        if (this.isGameWon()) {
          return this.crownIcon;
        } else {
          return this.hashtag;
        }
      } else if (this.gameResultCause == 'STALEMATE' || this.gameResultCause == 'INSUFFICIENT_MATERIAL' || this.gameResultCause == 'PLAYER_AGREEMENT') {
        return this.equals;
      }
    }
    return undefined;
  }

  isGameWonAndIsKing(): boolean {
    return this.isGameWon() && this.piece?.type == Piece.KING
  }

  isGameWon(): boolean {
    return (this.gameState == 'WHITE_WON' && this.piece?.color == "WHITE") || (this.gameState == 'BLACK_WON' && this.piece?.color == "BLACK")
  }

}
