import { Component, Input, OnDestroy, OnInit, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, Subscription, take } from 'rxjs';
import { BoardLegalMovesResponse } from '../../../shared/model/game/BoardLegalMovesResponse';
import { BoardView } from '../../../shared/model/game/BoardView';
import { CheckLegalMovesRequest } from '../../../shared/model/game/CheckLegalMovesRequest';
import { IllegalMoveResponse } from '../../../shared/model/game/IllegalMoveView';
import { MovePieceRequest } from '../../../shared/model/game/MovePieceRequest';
import { PieceDraggedInfo } from '../../../shared/model/game/PieceDraggedInfo';
import { RefreshBoardDto } from '../../../shared/model/game/RefreshBoardRequest';
import { ChessBoardWebsocketService } from '../../../shared/service/GameWebsocketService';
import { currentPlayerColorSelector, gameStateSelector, victoriousPlayerColorSelector } from '../../../shared/state/board-additional-info/board-additional-info.selectors';
import { boardLoaded, checkLegalMoves, draggedPieceChanged, draggedPieceReleased, gameFinished, movePiece, refreshBoard, refreshBoardWithPreMoves } from '../../../shared/state/board/board.actions';
import { boardGameObtainedInfoView, boardSelector, draggedPieceSelector, gameFinishedView, invalidMoveSelector, legalMovesSelector } from '../../../shared/state/board/board.selectors';
import { ChessGameState } from '../../../shared/state/chess-game.state';
import { BoardGameObtainedInfoView } from '../../../shared/model/game/BoardGameObtainedInfoView';
import { GameFinishedView } from '../../../shared/model/player-info/GameFinishedView';

@Component({
  selector: 'app-chess-board',
  templateUrl: './chess-board.component.html',
  styleUrls: ['./chess-board.component.scss']
})
export class ChessBoardComponent implements OnInit, OnDestroy {

  @Input() boardId: string;
  @Input() username: string
  
  private store = inject(Store<ChessGameState>)
  private chessBoardWebSocketService = inject(ChessBoardWebsocketService)

  boardView$: Observable<BoardView> = this.store.select(boardSelector);
  illegalMoveResponse$: Observable<IllegalMoveResponse> = this.store.select(invalidMoveSelector);
  draggedPieceInfo$: Observable<PieceDraggedInfo | undefined> = this.store.select(draggedPieceSelector)
  legalMoves$: Observable<BoardLegalMovesResponse | null | undefined> = this.store.select(legalMovesSelector)
  victoriousPlayerColor$: Observable<string | null | undefined> = this.store.select(victoriousPlayerColorSelector)
  gameState$: Observable<'CONTINUE' | 'CHECKMATE' | 'STALEMATE' | 'RESIGNED' | 'DRAW' | null | undefined> = this.store.select(gameStateSelector)
  currentPlayerColor$: Observable<string | null | undefined> = this.store.select(currentPlayerColorSelector)
  boardGameObtainedInfoView$: Observable<BoardGameObtainedInfoView | null | undefined> = this.store.select(boardGameObtainedInfoView)
  gameFinishedView$: Observable<GameFinishedView | null | undefined> = this.store.select(gameFinishedView)

  private chessTopicWithPreMovesSubscription$: Subscription
  private chessTopicSubscription$: Subscription
  private obtainStatusSubscription$: Subscription

  ranks: number[] = [8, 7, 6, 5, 4, 3, 2, 1]
  files: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

  isUsernamePlayerOnBoard: boolean

  ngOnInit(): void {
    
    this.boardView$.pipe(take(1)).subscribe(boardView => {
      setTimeout(
        async () => {
          if ((boardView.whiteUsername == this.username || boardView.blackUsername == this.username) && !this.isUsernamePlayerOnBoard) { 
            this.isUsernamePlayerOnBoard = true
            this.sendChessBoardWithPreMovesRefreshRequest()
          }
          this.chessTopicWithPreMovesSubscription$ = await this.chessBoardWebSocketService.subscribe(
            `/chess-topic/${this.boardId}/${this.username}`,
            (response: any) => {
              let boardView: BoardView = JSON.parse(response)
              this.store.dispatch(boardLoaded(boardView))
            }
          )
        }
      )
    })
    
    if (!this.isUsernamePlayerOnBoard) {
      setTimeout(
        async () => {
          this.chessTopicSubscription$ = await this.chessBoardWebSocketService.subscribe(
            `/chess-topic/${this.boardId}`,
            (response: any) => {
              let boardView: BoardView = JSON.parse(response)
              this.store.dispatch(boardLoaded(boardView))
            }
          )
        }
      )
    }

    setTimeout(
      async () => {
        this.obtainStatusSubscription$ = await this.chessBoardWebSocketService.subscribe(
          `/chess-topic/obtain-status/${this.boardId}`,
          (response: any) => {
            let boardGameObtainedInfoView: BoardGameObtainedInfoView = JSON.parse(response)
            this.store.dispatch(gameFinished(boardGameObtainedInfoView))
          } 
        )
      }
    )

  }

  ngOnDestroy(): void {
    this.chessTopicWithPreMovesSubscription$.unsubscribe()
    this.chessTopicSubscription$.unsubscribe()
    this.obtainStatusSubscription$.unsubscribe()
  }

  pieceDraggedChange(pieceDraggedInfo: PieceDraggedInfo) {
    this.store.dispatch(draggedPieceChanged(pieceDraggedInfo))
    let request: CheckLegalMovesRequest = {
      'boardId': this.boardId,
      'startSquare': pieceDraggedInfo.squareInfo.file + pieceDraggedInfo.squareInfo.rank,
      'pieceColor': pieceDraggedInfo.pieceInfo.color,
      'username': this.username
    }
    this.store.dispatch(checkLegalMoves(request))
  }

  pieceDroppedChange(moveRequest: MovePieceRequest) {
    this.store.dispatch(movePiece(moveRequest))
  }

  pieceDraggedRelease() {
    this.store.dispatch(draggedPieceReleased({}))
  }

  sendChessBoardWithPreMovesRefreshRequest() {
    let refreshBoardDto: RefreshBoardDto = {
      'boardId': this.boardId,
      'username': this.username
    }
    this.store.dispatch(refreshBoardWithPreMoves(refreshBoardDto))
  }

}
