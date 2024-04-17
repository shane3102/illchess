import { Component, Input } from '@angular/core';
import { Piece, PieceInfo } from '../../model/PieceInfo';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faCrown, faEquals, faHashtag } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-king-board-info-after-game-finished',
  templateUrl: './king-board-info-after-game-finished.component.html',
  styleUrls: ['./king-board-info-after-game-finished.component.scss']
})
export class KingBoardInfoAfterGameFinishedComponent {

  @Input() piece: PieceInfo | undefined;
  @Input() gameState: 'CONTINUE' | 'CHECKMATE' | 'STALEMATE' | null | undefined
  @Input() victoriousPlayerColor: string | null | undefined

  crownIcon = faCrown
  hashtag = faHashtag
  equals = faEquals

  Piece = Piece

  getShowedIcon(): IconDefinition | undefined {
    if (this.piece?.type == Piece.KING) {
      if (this.gameState == 'CHECKMATE') {
        if (this.piece?.color == this.victoriousPlayerColor) {
          return this.crownIcon;
        } else {
          return this.hashtag;
        }
      } else if (this.gameState == 'STALEMATE') {
        return this.equals;
      }
    }
    return undefined;
  }

  isGameWon(): boolean {
    return this.piece?.color == this.victoriousPlayerColor && this.piece?.type == Piece.KING
  }

}
