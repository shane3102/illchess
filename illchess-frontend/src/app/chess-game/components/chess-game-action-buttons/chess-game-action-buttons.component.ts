import { Component, Input, inject } from '@angular/core';
import { faAngleDoubleLeft, faFlag, faHandshake } from '@fortawesome/free-solid-svg-icons';
import { ChessGameState } from '../../../shared/state/chess-game.state';
import { Store } from '@ngrx/store';
import { acceptDraw, acceptTakingBackMove, proposeDraw, proposeTakingBackMove, rejectDraw, rejectTakingBackMove, resignGame } from '../../../shared/state/board-additional-info/board-additional-info.actions';
import { ResignGameRequest } from '../../../shared/model/game/ResignGameRequest';
import { PieceColor } from '../../../shared/model/game/PieceInfo';
import { PlayerView } from '../../../shared/model/game/BoardAdditionalInfoView';

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
  @Input() performedMoves: string[]
  @Input() gameState: 'CONTINUE' | 'WHITE_WON' | 'BLACK_WON' | 'DRAW'

  handshake = faHandshake
  flag = faFlag
  back = faAngleDoubleLeft

  PieceColor = PieceColor

  private store = inject(Store<ChessGameState>)

  resignGame() {
    let dto: ResignGameRequest = {'gameId': this.boardId, 'username': this.username}
    this.store.dispatch(resignGame(dto))
  }

  proposeDraw() {
    this.store.dispatch(proposeDraw({gameId: this.boardId, username: this.username}))
  }

  acceptDraw() {
    this.store.dispatch(acceptDraw({gameId: this.boardId, username: this.username}))
  }

  rejectDraw() {
    this.store.dispatch(rejectDraw({gameId: this.boardId, username: this.username}))
  }

  proposeTakeBackMove() {
    this.store.dispatch(proposeTakingBackMove({gameId: this.boardId, username: this.username}))
  }

  acceptTakingBackMove() {
    this.store.dispatch(acceptTakingBackMove({gameId: this.boardId, username: this.username}))
  }

  rejectTakingBackMove() {
    this.store.dispatch(rejectTakingBackMove({gameId: this.boardId, username: this.username}))
  }

}
