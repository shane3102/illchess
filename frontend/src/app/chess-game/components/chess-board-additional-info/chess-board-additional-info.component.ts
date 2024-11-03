import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { BoardAdditionalInfoView } from '../../../shared/model/game/BoardAdditionalInfoView';
import { PieceColor } from '../../../shared/model/game/PieceInfo';
import { RefreshBoardDto } from '../../../shared/model/game/RefreshBoardRequest';
import { ChessBoardWebsocketService } from '../../../shared/service/GameWebsocketService';
import { boardAdditionalInfoLoaded, refreshAdditionalInfoOfBoard } from '../../../shared/state/board-additional-info/board-additional-info.actions';
import { ChessGameState } from '../../../shared/state/chess-game.state';

@Component({
  selector: 'app-chess-board-additional-info',
  templateUrl: './chess-board-additional-info.component.html',
  styleUrls: ['./chess-board-additional-info.component.scss']
})
export class ChessBoardAdditionalInfoComponent implements OnInit {

  PieceColor = PieceColor

  @Input() boardId: string;
  @Input() username: string;
  @Input() boardAdditionalInfoView: BoardAdditionalInfoView | undefined | null

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
