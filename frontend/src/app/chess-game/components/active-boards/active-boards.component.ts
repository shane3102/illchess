import { Component, OnInit } from '@angular/core';
import { ChessBoardWebsocketService } from '../../service/ChessBoardWebsocketService';
import { Store } from '@ngrx/store';
import { ChessGameState } from '../../state/chess-game.state';
import { ActiveBoardsView } from '../../model/ActiveBoardsView';
import { activeBoardsRefreshed, refreshActiveBoards } from '../../state/active-boards/active-boards.actions';
import { Observable } from 'rxjs';
import { selectActiveBoards } from '../../state/active-boards/active-boards.selectors';
import { faCaretLeft, faCaretRight } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-active-boards',
  templateUrl: './active-boards.component.html',
  styleUrls: ['./active-boards.component.scss']
})
export class ActiveBoardsComponent implements OnInit {

  caretRight = faCaretRight
  caretLeft = faCaretLeft

  clickedLeft: boolean = false
  clickedRight: boolean = false

  page = 0

  activeBoardsView$: Observable<ActiveBoardsView> = this.store.select(selectActiveBoards)

  constructor(
    private store: Store<ChessGameState>,
    private chessBoardWebSocketService: ChessBoardWebsocketService
  ) {

  }

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

  isDisabled(side: 'left' | 'right', numberOfBoards: number) {
    if (side == 'right') {
      return (numberOfBoards) <= this.page + 3 || this.clickedRight;
    } else {
      return this.page <= 0 || this.clickedLeft
    }
  }

  slideRight() {
    this.clickedLeft = true
    setTimeout(() => {
      this.clickedLeft = false
      this.page = this.page + 3
    }, 900)
  }

  slideLeft() {
    this.clickedRight = true
    setTimeout(() => {
      this.clickedRight = false
      this.page = this.page - 3
    }, 900)
  }

  getSliceFrom() {
    return Math.max(this.page-3, 0)
  }

}
