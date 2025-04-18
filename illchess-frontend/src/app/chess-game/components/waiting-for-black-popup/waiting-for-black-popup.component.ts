import { Component, inject, Input } from '@angular/core';
import { Store } from '@ngrx/store';
import { GameView } from 'src/app/shared/model/game/BoardView';
import { quitNotYetStartedGame } from 'src/app/shared/state/board/board.actions';
import { ChessGameState } from 'src/app/shared/state/chess-game.state';

@Component({
  selector: 'app-waiting-for-black-popup',
  templateUrl: './waiting-for-black-popup.component.html',
  styleUrls: ['./waiting-for-black-popup.component.scss']
})
export class WaitingForBlackPopupComponent {

  @Input() boardId: string;
  @Input() username: string

  private store = inject(Store<ChessGameState>)

  quitNotYetStartedGame() {
    this.store.dispatch(quitNotYetStartedGame({boardId: this.boardId, username: this.username}))
  }
}
