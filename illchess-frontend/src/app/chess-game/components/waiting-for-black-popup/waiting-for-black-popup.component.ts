import { Component, inject, Input } from '@angular/core';
import { Store } from '@ngrx/store';
import { quitNotYetStartedGame } from 'src/app/shared/state/board/board.actions';
import { ChessGameState } from 'src/app/shared/state/chess-game.state';

@Component({
  selector: 'app-waiting-for-black-popup',
  templateUrl: './waiting-for-black-popup.component.html',
  styleUrls: ['./waiting-for-black-popup.component.scss']
})
export class WaitingForBlackPopupComponent {

  @Input() gameId: string;
  @Input() username: string

  private store = inject(Store<ChessGameState>)

  quitNotYetStartedGame() {
    this.store.dispatch(quitNotYetStartedGame({gameId: this.gameId, username: this.username}))
  }
}
