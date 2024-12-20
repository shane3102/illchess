import { Component, Input, OnDestroy, OnInit, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { BoardAdditionalInfoView } from '../../../shared/model/game/BoardAdditionalInfoView';
import { PieceColor } from '../../../shared/model/game/PieceInfo';
import { RefreshBoardDto } from '../../../shared/model/game/RefreshBoardRequest';
import { ChessBoardWebsocketService } from '../../../shared/service/GameWebsocketService';
import { boardAdditionalInfoLoaded, refreshAdditionalInfoOfBoard } from '../../../shared/state/board-additional-info/board-additional-info.actions';
import { ChessGameState } from '../../../shared/state/chess-game.state';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-chess-board-additional-info',
  templateUrl: './chess-board-additional-info.component.html',
  styleUrls: ['./chess-board-additional-info.component.scss']
})
export class ChessBoardAdditionalInfoComponent implements OnInit, OnDestroy {

  PieceColor = PieceColor

  @Input() boardId: string;
  @Input() username: string;
  @Input() boardAdditionalInfoView: BoardAdditionalInfoView | undefined | null

  private store = inject(Store<ChessGameState>)
  private chessBoardWebSocketService = inject(ChessBoardWebsocketService)
  private boardAdditionalInfoSubscription$: Subscription

  ngOnInit(): void {
    this.sendChessBoardAdditionalInfoRefreshRequest()
    setTimeout(
      async () => {
        this.boardAdditionalInfoSubscription$ = await this.chessBoardWebSocketService.subscribe(
          `/chess-topic/additional-info/${this.boardId}`,
          (response: any) => {
            let boardAdditionalInfoView: BoardAdditionalInfoView = JSON.parse(response)
            this.store.dispatch(boardAdditionalInfoLoaded(boardAdditionalInfoView))
          }
        )
      }
    )
  }

  ngOnDestroy(): void {
    this.boardAdditionalInfoSubscription$.unsubscribe()
  }

  sendChessBoardAdditionalInfoRefreshRequest() {
    let refreshBoardDto: RefreshBoardDto = {
      'boardId': this.boardId
    }
    this.store.dispatch(refreshAdditionalInfoOfBoard(refreshBoardDto))
  }

}
