import { Component, OnInit } from '@angular/core';
import { PieceDraggedInfo } from '../../model/PieceDraggedInfo';
import { SquareInfo } from '../../model/SquareInfo';
import { Observable, Subject, of } from 'rxjs';
import { BoardView } from '../../model/BoardView';
import { v4 as uuidv4 } from 'uuid';
import { IllegalMoveView } from '../../model/IllegalMoveView';
import { MovePieceRequest } from '../../model/MovePieceRequest';
import { InitializeBoardRequest } from '../../model/InitializeBoardRequest';
import { Store } from '@ngrx/store';
import { checkLegalMoves, draggedPieceChanged, draggedPieceReleased, initializeBoard, movePiece } from '../../state/board/board.actions';
import { boardSelector, draggedPieceSelector, invalidMoveSelector, legalMovesSelector } from '../../state/board/board.selectors';
import { ChessGameState } from '../../state/chess-game.state';
import { CheckLegalMovesRequest } from '../../model/CheckLegalMovesRequest';
import { BoardLegalMovesResponse } from '../../model/BoardLegalMovesResponse';

@Component({
  selector: 'app-chess-board',
  templateUrl: './chess-board.component.html',
  styleUrls: ['./chess-board.component.scss']
})
export class ChessBoardComponent implements OnInit {

  boardId: string = uuidv4()
  boardView: Observable<BoardView> = this.store.select(boardSelector);
  illegalMoveView: Observable<IllegalMoveView> = this.store.select(invalidMoveSelector);
  draggedPieceInfo: Observable<PieceDraggedInfo | undefined> = this.store.select(draggedPieceSelector)
  legalMoves: Observable<BoardLegalMovesResponse | undefined> = this.store.select(legalMovesSelector)

  illegalMoveViewSubject: Subject<IllegalMoveView> = new Subject<IllegalMoveView>();

  ranks: number[] = [8, 7, 6, 5, 4, 3, 2, 1]
  files: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

  constructor(private store: Store<ChessGameState>) { }

  ngOnInit(): void {
    setTimeout(() => this.sendChessBoardInitializeRequest(), 500)
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

  sendChessBoardInitializeRequest() {

    let initializeNewBoardRequest: InitializeBoardRequest = {
      'newBoardId': this.boardId
    }
    this.store.dispatch(initializeBoard(initializeNewBoardRequest))
  }

  displayInfoWithIllegalMove(illegalMoveView: any) {
    let illegalMoveViewParsed: IllegalMoveView = JSON.parse(illegalMoveView.body)
    this.illegalMoveViewSubject.next(illegalMoveViewParsed)
  }

}

