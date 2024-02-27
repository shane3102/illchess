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
import { initializeBoard, movePiece } from '../../state/board/board.actions';
import { boardSelector, invalidMoveSelector } from '../../state/board/board.selectors';
import { ChessGameState } from '../../state/chess-game.state';

@Component({
  selector: 'app-chess-board',
  templateUrl: './chess-board.component.html',
  styleUrls: ['./chess-board.component.scss']
})
export class ChessBoardComponent implements OnInit {

  boardId: string = uuidv4()
  boardView: Observable<BoardView>= this.store.select(boardSelector);
  illegalMoveView: Observable<IllegalMoveView> = this.store.select(invalidMoveSelector)

  illegalMoveViewSubject: Subject<IllegalMoveView> = new Subject<IllegalMoveView>();

  ranks: number[] = [8, 7, 6, 5, 4, 3, 2, 1]
  files: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

  currentllyDraggedPiece: PieceDraggedInfo;

  constructor(private store: Store<ChessGameState>) { }

  ngOnInit(): void {
    setTimeout(() => this.sendChessBoardInitializeRequest(), 500)
  }

  pieceDraggedChange(pieceDraggedInfo: PieceDraggedInfo) {
    this.currentllyDraggedPiece = pieceDraggedInfo;
  }

  pieceDroppedChange(pieceDroppedInfo: SquareInfo) {
    this.sendChessBoardUpdateRequest(pieceDroppedInfo)
  }

  sendChessBoardUpdateRequest(pieceDroppedInfo: SquareInfo) {

    let moveRequest: MovePieceRequest = {
      'boardId': this.boardId,
      'startSquare': this.currentllyDraggedPiece.squareInfo.file + this.currentllyDraggedPiece.squareInfo.rank,
      'targetSquare': pieceDroppedInfo.file + pieceDroppedInfo.rank,
      'pieceColor': this.currentllyDraggedPiece.pieceInfo.color,
      'pieceType': this.currentllyDraggedPiece.pieceInfo.type
    }
    this.store.dispatch(movePiece(moveRequest))
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

