import { Component, OnInit, inject } from '@angular/core';
import { ChessBoardWebsocketService } from '../../service/ChessBoardWebsocketService';
import { Store } from '@ngrx/store';
import { ChessGameState } from '../../state/chess-game.state';
import { ActiveBoardsView } from '../../model/ActiveBoardsView';
import { activeBoardsRefreshed, refreshActiveBoards } from '../../state/active-boards/active-boards.actions';
import { Observable } from 'rxjs';
import { selectActiveBoards } from '../../state/active-boards/active-boards.selectors';
import { faCaretLeft, faCaretRight, faExclamationCircle } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';

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

  page = 0

  activeBoardsView$: Observable<ActiveBoardsView> = this.store.select(selectActiveBoards)


  ngOnInit(): void {
    this.store.dispatch(refreshActiveBoards({}))
    this.chessBoardWebSocketService.subscribe(
      `/chess-topic/active-boards`,
      (response: any) => {
        let activeBoardsView: ActiveBoardsView = JSON.parse(response)
        this.store.dispatch(activeBoardsRefreshed(activeBoardsView))
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
      return (numberOfBoards) <= this.page + this.pageSize || this.clickedRight;
    } else {
      return this.page <= 0 || this.clickedLeft
    }
  }

  slideRight() {
    this.clickedLeft = true
    setTimeout(() => {
      this.clickedLeft = false
      this.page = this.page + this.pageSize
    }, 900)
  }

  slideLeft() {
    this.clickedRight = true
    setTimeout(() => {
      this.clickedRight = false
      this.page = this.page - this.pageSize
    }, 900)
  }

  getSliceFrom() {
    return Math.max(this.page-this.pageSize, 0)
  }

}
