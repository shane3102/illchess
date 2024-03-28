import { Component, OnInit } from '@angular/core';
import { ChessBoardWebsocketService } from '../../service/ChessBoardWebsocketService';
import { Store } from '@ngrx/store';
import { ChessGameState } from '../../state/chess-game.state';
import { ActiveBoardsView } from '../../model/ActiveBoardsView';
import { activeBoardsRefreshed, refreshActiveBoards } from '../../state/active-boards/active-boards.actions';
import { Observable } from 'rxjs';
import { selectActiveBoards } from '../../state/active-boards/active-boards.selectors';

@Component({
  selector: 'app-active-boards',
  templateUrl: './active-boards.component.html',
  styleUrls: ['./active-boards.component.scss']
})
export class ActiveBoardsComponent implements OnInit {

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

}
