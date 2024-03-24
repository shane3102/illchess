import { Component, OnInit } from '@angular/core';
import { PieceDraggedInfo } from '../../model/PieceDraggedInfo';
import { SquareInfo } from '../../model/SquareInfo';
import { Observable, Subject, of } from 'rxjs';
import { BoardView } from '../../model/BoardView';
import { v4 as uuidv4 } from 'uuid';
import { IllegalMoveResponse } from '../../model/IllegalMoveView';
import { MovePieceRequest } from '../../model/MovePieceRequest';
import { InitializeBoardRequest } from '../../model/InitializeBoardRequest';
import { Store } from '@ngrx/store';
import { boardLoaded, checkLegalMoves, draggedPieceChanged, draggedPieceReleased, initializeBoard, movePiece, refreshBoard } from '../../state/board/board.actions';
import { boardSelector, draggedPieceSelector, invalidMoveSelector, legalMovesSelector } from '../../state/board/board.selectors';
import { ChessGameState } from '../../state/chess-game.state';
import { CheckLegalMovesRequest } from '../../model/CheckLegalMovesRequest';
import { BoardLegalMovesResponse } from '../../model/BoardLegalMovesResponse';
import { ChessBoardWebsocketService } from '../../service/ChessBoardWebsocketService';
import { ActivatedRoute } from '@angular/router';
import { RefreshBoardDto } from '../../model/RefreshBoardRequest';

@Component({
  selector: 'app-chess-board',
  templateUrl: './chess-board.component.html',
  styleUrls: ['./chess-board.component.scss']
})
export class ChessBoardComponent implements OnInit {

  boardId: string;
  username: string

  boardView: Observable<BoardView> = this.store.select(boardSelector);
  illegalMoveResponse: Observable<IllegalMoveResponse> = this.store.select(invalidMoveSelector);
  draggedPieceInfo: Observable<PieceDraggedInfo | undefined> = this.store.select(draggedPieceSelector)
  legalMoves: Observable<BoardLegalMovesResponse | null | undefined> = this.store.select(legalMovesSelector)

  ranks: number[] = [8, 7, 6, 5, 4, 3, 2, 1]
  files: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

  constructor(
    private store: Store<ChessGameState>,
    private route: ActivatedRoute,
    private chessBoardWebSocketService: ChessBoardWebsocketService
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(
      params => {
        this.boardId = params['boardId']
        this.username =  params['username']
        if (this.boardId) {
          this.sendChessBoardRefreshRequest()
          this.chessBoardWebSocketService.subscribe(
            `/chess-topic/${this.boardId}`,
            (response: any) => {
              let boardView: BoardView = JSON.parse(response)
              this.store.dispatch(boardLoaded(boardView))
            }
          )
        }
      }
    )
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
    let initializeNewBoardRequest: RefreshBoardDto = {
      'boardId': this.boardId
    }
    this.store.dispatch(refreshBoard(initializeNewBoardRequest))
  }

}

