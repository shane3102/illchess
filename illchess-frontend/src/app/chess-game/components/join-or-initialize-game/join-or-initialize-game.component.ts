import { Component, inject, Input, OnInit, Signal } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { takeWhile } from 'rxjs';
import { InitializeNewGameRequest } from '../../../shared/model/game/InitializeNewGameRequest';
import { RefreshBoardDto } from '../../../shared/model/game/RefreshBoardRequest';
import { initializeBoard, refreshBoard } from '../../../shared/state/board/board.actions';
import { initializedGameIdSelector } from '../../../shared/state/board/board.selectors';
import { ChessGameState } from '../../../shared/state/chess-game.state';
import { toSignal } from '@angular/core/rxjs-interop';
import { username } from 'src/app/shared/state/player-info/player-info.selectors';

@Component({
  selector: 'app-join-or-initialize-game',
  templateUrl: './join-or-initialize-game.component.html',
  styleUrls: ['./join-or-initialize-game.component.scss']
})
export class JoinOrInitializeGameComponent implements OnInit {
  
  private store = inject(Store<ChessGameState>)
  private router = inject(Router)
  
  gameId: string
  username: Signal<string | undefined> = toSignal(this.store.select(username))

  ngOnInit(): void {
    let initializeNewBoardRequest: InitializeNewGameRequest = {
      'username': this.username()!
    }
    this.store.dispatch(initializeBoard(initializeNewBoardRequest))

    this.store.select(initializedGameIdSelector)
      .pipe(takeWhile(() => !this.gameId))
      .subscribe(
        (dto: RefreshBoardDto) => {
          if (dto.gameId) {
            this.store.dispatch(refreshBoard(dto))
            this.router.navigateByUrl(`/game/${dto.gameId}`)
            this.gameId = dto.gameId
          }
        }
      )
  }

}
