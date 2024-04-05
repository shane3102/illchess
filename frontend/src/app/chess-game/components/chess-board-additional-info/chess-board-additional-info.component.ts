import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { ChessGameState } from '../../state/chess-game.state';
import { ChessBoardWebsocketService } from '../../service/ChessBoardWebsocketService';
import { BoardAdditionalInfoView } from '../../model/BoardAdditionalInfoView';
import { boardAdditionalInfoLoaded, refreshAdditionalInfoOfBoard } from '../../state/board-additional-info/board-additional-info.actions';
import { RefreshBoardDto } from '../../model/RefreshBoardRequest';
import { Observable } from 'rxjs';
import { boardAdditionalInfoSelector } from '../../state/board-additional-info/board-additional-info.selectors';

@Component({
  selector: 'app-chess-board-additional-info',
  templateUrl: './chess-board-additional-info.component.html',
  styleUrls: ['./chess-board-additional-info.component.scss']
})
export class ChessBoardAdditionalInfoComponent implements OnInit {

  @Input() boardId: string;
  @Input() username: string;

  boardAdditionalInfoView$: Observable<BoardAdditionalInfoView> = this.store.select(boardAdditionalInfoSelector)

  constructor(
    private store: Store<ChessGameState>,
    private chessBoardWebSocketService: ChessBoardWebsocketService
  ) {
  }

  ngOnInit(): void {
    this.sendChessBoardAdditionalInfoRefreshRequest()
    this.chessBoardWebSocketService.subscribe(
      `/chess-topic/additional-info/${this.boardId}`,
      (response: any) => {
        let boardAdditionalInfoView: BoardAdditionalInfoView = JSON.parse(response)
        this.store.dispatch(boardAdditionalInfoLoaded(boardAdditionalInfoView))
      }
    )
  }

  sendChessBoardAdditionalInfoRefreshRequest() {
    let refreshBoardDto: RefreshBoardDto = {
      'boardId': this.boardId
    }
    this.store.dispatch(refreshAdditionalInfoOfBoard(refreshBoardDto))
  }

}
