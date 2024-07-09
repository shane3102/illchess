import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { BoardLegalMovesResponse } from '../../model/BoardLegalMovesResponse';
import { BoardView } from '../../model/BoardView';
import { CheckLegalMovesRequest } from '../../model/CheckLegalMovesRequest';
import { IllegalMoveResponse } from '../../model/IllegalMoveView';
import { MovePieceRequest } from '../../model/MovePieceRequest';
import { PieceDraggedInfo } from '../../model/PieceDraggedInfo';
import { RefreshBoardDto } from '../../model/RefreshBoardRequest';
import { ChessBoardWebsocketService } from '../../service/ChessBoardWebsocketService';
import { boardLoaded, checkLegalMoves, draggedPieceChanged, draggedPieceReleased, movePiece, refreshBoard, refreshBoardWithPreMoves } from '../../state/board/board.actions';
import { boardSelector, draggedPieceSelector, invalidMoveSelector, legalMovesSelector } from '../../state/board/board.selectors';
import { ChessGameState } from '../../state/chess-game.state';
import { gameStateSelector, victoriousPlayerColorSelector } from '../../state/board-additional-info/board-additional-info.selectors';

@Component({
  selector: 'app-chess-board',
  templateUrl: './chess-board.component.html',
  styleUrls: ['./chess-board.component.scss']
})
export class ChessBoardComponent implements OnInit {

  @Input() boardId: string;
  @Input() username: string

  boardView$: Observable<BoardView> = this.store.select(boardSelector);
  illegalMoveResponse$: Observable<IllegalMoveResponse> = this.store.select(invalidMoveSelector);
  draggedPieceInfo$: Observable<PieceDraggedInfo | undefined> = this.store.select(draggedPieceSelector)
  legalMoves$: Observable<BoardLegalMovesResponse | null | undefined> = this.store.select(legalMovesSelector)
  victoriousPlayerColor$: Observable<string | null | undefined> = this.store.select(victoriousPlayerColorSelector)
  gameState$: Observable<'CONTINUE' | 'CHECKMATE' | 'STALEMATE' | 'RESIGNED' | 'DRAW' | null | undefined> = this.store.select(gameStateSelector)

  ranks: number[] = [8, 7, 6, 5, 4, 3, 2, 1]
  files: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

  isUsernamePlayerOnBoard: boolean

  constructor(
    private store: Store<ChessGameState>,
    private chessBoardWebSocketService: ChessBoardWebsocketService
  ) {
  }

  ngOnInit(): void {
    this.sendChessBoardRefreshRequest()
    this.boardView$.subscribe(boardView => {
      if ((boardView.whiteUsername == this.username || boardView.blackUsername == this.username) && !this.isUsernamePlayerOnBoard) {
        this.isUsernamePlayerOnBoard = true
        this.sendChessBoardWithPreMovesRefreshRequest()
        this.chessBoardWebSocketService.subscribe(
          `/chess-topic/${this.boardId}/${this.username}`,
          (response: any) => {
            let boardView: BoardView = JSON.parse(response)
            this.store.dispatch(boardLoaded(boardView))
          }
        )
      }
    })
    if (!this.isUsernamePlayerOnBoard) {
      this.chessBoardWebSocketService.subscribe(
        `/chess-topic/${this.boardId}`,
        (response: any) => {
          let boardView: BoardView = JSON.parse(response)
          this.store.dispatch(boardLoaded(boardView))
        }
      )
    }

  }

  pieceDraggedChange(pieceDraggedInfo: PieceDraggedInfo) {
    this.store.dispatch(draggedPieceChanged(pieceDraggedInfo))
    let request: CheckLegalMovesRequest = {
      'boardId': this.boardId,
      'startSquare': pieceDraggedInfo.squareInfo.file + pieceDraggedInfo.squareInfo.rank,
      'pieceColor': pieceDraggedInfo.pieceInfo.color,
      'pieceType': pieceDraggedInfo.pieceInfo.type
    }
    this.store.dispatch(checkLegalMoves(request))
  }

  pieceDroppedChange(moveRequest: MovePieceRequest) {
    this.store.dispatch(movePiece(moveRequest))
  }

  pieceDraggedRelease() {
    this.store.dispatch(draggedPieceReleased({}))
  }

  sendChessBoardRefreshRequest() {
    let refreshBoardDto: RefreshBoardDto = {
      'boardId': this.boardId
    }
    this.store.dispatch(refreshBoard(refreshBoardDto))
  }

  sendChessBoardWithPreMovesRefreshRequest() {
    let refreshBoardDto: RefreshBoardDto = {
      'boardId': this.boardId,
      'username': this.username
    }
    this.store.dispatch(refreshBoardWithPreMoves(refreshBoardDto))
  }

}

