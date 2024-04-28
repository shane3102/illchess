import { Component, Input } from '@angular/core';
import { faAngleDoubleLeft, faFlag, faHandshake } from '@fortawesome/free-solid-svg-icons';
import { ChessGameState } from '../../state/chess-game.state';
import { Store } from '@ngrx/store';
import { acceptDraw, proposeDraw, rejectDraw, resignGame } from '../../state/board-additional-info/board-additional-info.actions';
import { ResignGameRequest } from '../../model/ResignGameRequest';
import { PieceColor } from '../../model/PieceInfo';
import { PlayerView } from '../../model/BoardAdditionalInfoView';

@Component({
  selector: 'app-chess-game-action-buttons',
  templateUrl: './chess-game-action-buttons.component.html',
  styleUrls: ['./chess-game-action-buttons.component.scss']
})
export class ChessGameActionButtonsComponent {

  @Input() boardId: string
  @Input() username: string;
  @Input() whitePlayer: PlayerView | undefined
  @Input() blackPlayer: PlayerView | undefined
  @Input() color: PieceColor

  handshake = faHandshake
  flag = faFlag
  back = faAngleDoubleLeft

  PieceColor = PieceColor

  constructor(private store: Store<ChessGameState>) {
  }

  resignGame() {
    let dto: ResignGameRequest = {'boardId': this.boardId, 'username': this.username}
    this.store.dispatch(resignGame(dto))
  }

  proposeDraw() {
    this.store.dispatch(proposeDraw({boardId: this.boardId, username: this.username}))
  }

  acceptDraw() {
    this.store.dispatch(acceptDraw({boardId: this.boardId, username: this.username}))
  }

  rejectDraw() {
    this.store.dispatch(rejectDraw({boardId: this.boardId, username: this.username}))
  }

}
