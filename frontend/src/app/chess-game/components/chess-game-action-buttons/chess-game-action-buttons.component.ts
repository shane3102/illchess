import { Component, Input } from '@angular/core';
import { faAngleDoubleLeft, faFlag, faHandshake } from '@fortawesome/free-solid-svg-icons';
import { ChessGameState } from '../../state/chess-game.state';
import { Store } from '@ngrx/store';
import { resignGame } from '../../state/board-additional-info/board-additional-info.actions';
import { ResignGameRequest } from '../../model/ResignGameRequest';

@Component({
  selector: 'app-chess-game-action-buttons',
  templateUrl: './chess-game-action-buttons.component.html',
  styleUrls: ['./chess-game-action-buttons.component.scss']
})
export class ChessGameActionButtonsComponent {

  @Input() boardId: string
  @Input() username: string;

  handshake = faHandshake
  flag = faFlag
  back = faAngleDoubleLeft

  constructor(private store: Store<ChessGameState>) {
  }

  resignGame() {
    let dto: ResignGameRequest = {'boardId': this.boardId, 'username': this.username}
    this.store.dispatch(resignGame(dto))
  }

}
