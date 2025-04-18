import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { faCaretLeft, faCaretRight, faExclamationCircle } from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { GameObtainedInfoView } from 'src/app/shared/model/game/BoardGameObtainedInfoView';
import { ActiveGameNewView } from '../../../shared/model/game/ActiveBoardNewView';
import { ActiveGamesView } from '../../../shared/model/game/ActiveBoardsView';
import { ChessBoardWebsocketService } from '../../../shared/service/GameWebsocketService';
import { newActiveBoardObtained, refreshActiveBoards, removeFinishedBoardFromActiveBoard } from '../../../shared/state/active-boards/active-boards.actions';
import { selectActiveBoards } from '../../../shared/state/active-boards/active-boards.selectors';
import { ChessGameState } from '../../../shared/state/chess-game.state';

@Component({
  selector: 'app-active-boards',
  templateUrl: './active-boards.component.html',
  styleUrls: ['./active-boards.component.scss']
})
export class ActiveBoardsComponent implements OnInit, OnDestroy {

  pageSize = 4

  router = inject(Router)
  store = inject(Store<ChessGameState>)
  chessBoardWebSocketService = inject(ChessBoardWebsocketService)

  caretRight = faCaretRight
  caretLeft = faCaretLeft
  exclamationCircle = faExclamationCircle

  clickedLeft: boolean = false
  clickedRight: boolean = false
  clickingDisabled: boolean = false

  page = 0

  activeBoardsView$: Observable<ActiveGamesView> = this.store.select(selectActiveBoards)
  newActiveBoardSubscription$: Subscription
  obtainStatusSubscription$: Subscription

  ngOnInit(): void {
    this.store.dispatch(refreshActiveBoards({}))
    setTimeout(
      async () => {
        this.newActiveBoardSubscription$ = await this.chessBoardWebSocketService.subscribe(
          `/chess-topic/new-active-board`,
          (response: any) => {
            let activeBoardNewView: ActiveGameNewView = JSON.parse(response)
            this.store.dispatch(newActiveBoardObtained(activeBoardNewView))
          }
        )

        this.obtainStatusSubscription$ = await this.chessBoardWebSocketService.subscribe(
          `/chess-topic/obtain-status`,
          (response: any) => {
            let boardGameObtainedInfoView: GameObtainedInfoView = JSON.parse(response)
            setTimeout(
              () => { this.store.dispatch(removeFinishedBoardFromActiveBoard({ boardId: boardGameObtainedInfoView.gameId })) },
              1000
            )
          }
        )
      }
    )
  }

  ngOnDestroy(): void {
    this.newActiveBoardSubscription$.unsubscribe()
    this.obtainStatusSubscription$.unsubscribe()
  }

  spectateBoard(boardId: string) {
    this.router.navigateByUrl(`/game/${boardId}`)
  }

  isDisabled(side: 'left' | 'right', numberOfBoards: number) {
    if (side == 'right') {
      return (numberOfBoards) <= this.page + this.pageSize || this.clickingDisabled;
    } else {
      return this.page <= 0 || this.clickingDisabled
    }
  }

  slideRight() {
    this.clickingDisabled = true
    this.clickedLeft = true
    setTimeout(() => {
      this.clickedLeft = false
      this.clickingDisabled = false
      this.page = this.page + this.pageSize
    }, 900)
  }

  slideLeft() {
    this.clickingDisabled = true
    this.clickedRight = true
    setTimeout(() => {
      this.clickedRight = false
      this.clickingDisabled = false
      this.page = this.page - this.pageSize
    }, 900)
  }

  getSliceFrom(): number {
    return Math.max(this.page - this.pageSize, 0)
  }

  getSliceTo(): number {
    return this.page + 2 * this.pageSize
  }

}
