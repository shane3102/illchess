import { Component, inject, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { ChessGameState } from '../../../shared/state/chess-game.state';
import { initializeBoard, refreshBoard } from '../../../shared/state/board/board.actions';
import { InitializeBoardRequest } from '../../../shared/model/game/InitializeBoardRequest';
import { initializedBoardIdSelector } from '../../../shared/state/board/board.selectors';
import { Router } from '@angular/router';
import { RefreshBoardDto } from '../../../shared/model/game/RefreshBoardRequest';
import { takeUntil, takeWhile } from 'rxjs';

@Component({
  selector: 'app-join-or-initialize-game',
  templateUrl: './join-or-initialize-game.component.html',
  styleUrls: ['./join-or-initialize-game.component.scss']
})
export class JoinOrInitializeGameComponent implements OnInit {

  boardId: string
  randomNames: string[] = ["Mark", "Tom", "Pablo", "Jose", "William"]

  username: string = this.randomNames[Math.floor(Math.random() * this.randomNames.length)] + Math.floor(100 * Math.random())

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
            this.router.navigateByUrl(`/game/${dto.boardId}/${this.username}`)
            this.boardId = dto.boardId
          }
        }
      )
  }

}
