import { Component, OnInit, inject } from '@angular/core';
import { ChessBoardWebsocketService } from '../../../shared/service/ChessBoardWebsocketService';
import { Store } from '@ngrx/store';
import { ChessGameState } from '../../../shared/state/chess-game.state';
import { ActiveBoardsView } from '../../../shared/model/ActiveBoardsView';
import { activeBoardsRefreshed, newActiveBoardObtained, refreshActiveBoards } from '../../../shared/state/active-boards/active-boards.actions';
import { Observable } from 'rxjs';
import { selectActiveBoards } from '../../../shared/state/active-boards/active-boards.selectors';
import { faCaretLeft, faCaretRight, faExclamationCircle } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';
import { ActiveBoardNewView } from '../../../shared/model/ActiveBoardNewView';

@Component({
  selector: 'app-active-boards',
  templateUrl: './active-boards.component.html',
  styleUrls: ['./active-boards.component.scss']
})
export class ActiveBoardsComponent implements OnInit {

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

  activeBoardsView$: Observable<ActiveBoardsView> = this.store.select(selectActiveBoards)

  ngOnInit(): void {
    this.store.dispatch(refreshActiveBoards({}))
    this.chessBoardWebSocketService.subscribe(
      `/chess-topic/new-active-board`,
      (response: any) => {
        let activeBoardNewView: ActiveBoardNewView = JSON.parse(response)
        this.store.dispatch(newActiveBoardObtained(activeBoardNewView))
      }
    )
  }

  spectateBoard(boardId: string) {
    let randomNames: string[] = ["Mark", "Tom", "Pablo", "Jose", "William"]
    let username: string = randomNames[Math.floor(Math.random() * randomNames.length)] + Math.floor(100 * Math.random())

    this.router.navigateByUrl(`/game/${boardId}/${username}`)
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

  getSliceFrom() {
    return Math.max(this.page-this.pageSize, 0)
  }

}
