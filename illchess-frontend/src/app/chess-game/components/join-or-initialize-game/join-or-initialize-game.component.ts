import { Component, inject, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { takeWhile } from 'rxjs';
import { InitializeBoardRequest } from '../../../shared/model/game/InitializeBoardRequest';
import { RefreshBoardDto } from '../../../shared/model/game/RefreshBoardRequest';
import { initializeBoard, refreshBoard } from '../../../shared/state/board/board.actions';
import { initializedBoardIdSelector } from '../../../shared/state/board/board.selectors';
import { ChessGameState } from '../../../shared/state/chess-game.state';

@Component({
  selector: 'app-join-or-initialize-game',
  templateUrl: './join-or-initialize-game.component.html',
  styleUrls: ['./join-or-initialize-game.component.scss']
})
export class JoinOrInitializeGameComponent implements OnInit {

  boardId: string
  username: string = sessionStorage.getItem('username')!

  private store = inject(Store<ChessGameState>)
  private router = inject(Router)

  ngOnInit(): void {
    let initializeNewBoardRequest: InitializeBoardRequest = {
      'username': this.username
    }
    this.store.dispatch(initializeBoard(initializeNewBoardRequest))

    this.store.select(initializedBoardIdSelector)
      .pipe(takeWhile(() => !this.boardId))
      .subscribe(
        (dto: RefreshBoardDto) => {
          if (dto.boardId) {
            this.store.dispatch(refreshBoard(dto))
            this.router.navigateByUrl(`/game/${dto.boardId}`)
            this.boardId = dto.boardId
          }
        }
      )
  }

}
