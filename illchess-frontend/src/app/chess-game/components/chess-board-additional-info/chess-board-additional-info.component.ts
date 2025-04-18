import { Component, Input, OnDestroy, OnInit, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { GameAdditionalInfoView } from '../../../shared/model/game/BoardAdditionalInfoView';
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

  @Input() gameId: string;
  @Input() username: string;
  @Input() boardAdditionalInfoView: GameAdditionalInfoView | undefined | null

  private store = inject(Store<ChessGameState>)
  private chessBoardWebSocketService = inject(ChessBoardWebsocketService)
  private boardAdditionalInfoSubscription$: Subscription

  ngOnInit(): void {
    this.sendChessBoardAdditionalInfoRefreshRequest()
    setTimeout(
      async () => {
        this.boardAdditionalInfoSubscription$ = await this.chessBoardWebSocketService.subscribe(
          `/chess-topic/additional-info/${this.gameId}`,
          (response: any) => {
            let boardAdditionalInfoView: GameAdditionalInfoView = JSON.parse(response)
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
      'gameId': this.gameId
    }
    this.store.dispatch(refreshAdditionalInfoOfBoard(refreshBoardDto))
  }

}
