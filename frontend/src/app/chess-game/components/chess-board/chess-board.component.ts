import { Component, OnInit } from '@angular/core';
import { PieceColor, PieceInfo } from '../../model/PieceInfo';
import { PieceDraggedInfo } from '../../model/PieceDraggedInfo';
import { SquareInfo } from '../../model/SquareInfo';
import { Subject } from 'rxjs';
import { StompService } from '../../service/StompService';
import { BoardView } from '../../model/BoardView';
import { v4 as uuidv4 } from 'uuid';
import { IllegalMoveView } from '../../model/IllegalMoveView';

@Component({
  selector: 'app-chess-board',
  templateUrl: './chess-board.component.html',
  styleUrls: ['./chess-board.component.scss']
})
export class ChessBoardComponent implements OnInit {

  boardView: BoardView;

  illegalMoveViewSubject: Subject<IllegalMoveView> = new Subject<IllegalMoveView>();

  ranks: number[] = [8, 7, 6, 5, 4, 3, 2, 1]
  files: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

  currentllyDraggedPiece: PieceDraggedInfo;

  constructor(
    private stompService: StompService,
  ) {

  }

  ngOnInit(): void {
    this.stompService.subscribe("/chess-topic", (boardView: BoardView) => this.updateChessBoard(boardView))
    this.stompService.subscribe("/illegal-move", (illegalMoveView: IllegalMoveView) => this.displayInfoWithIllegalMove(illegalMoveView))
    setTimeout(() => this.sendChessBoardInitializeRequest(), 1000)
  }

  pieceDraggedChange(pieceDraggedInfo: PieceDraggedInfo) {
    this.currentllyDraggedPiece = pieceDraggedInfo;
  }

  pieceDroppedChange(pieceDroppedInfo: SquareInfo) {
    this.sendChessBoardUpdateRequest(pieceDroppedInfo)
  }

  sendChessBoardUpdateRequest(pieceDroppedInfo: SquareInfo) {

    let moveRequest = {
      'boardId': this.boardView.boardId,
      'startSquare': this.currentllyDraggedPiece.squareInfo.file + this.currentllyDraggedPiece.squareInfo.rank,
      'targetSquare': pieceDroppedInfo.file + pieceDroppedInfo.rank,
      'pieceColor': this.currentllyDraggedPiece.pieceInfo.color,
      'pieceType': this.currentllyDraggedPiece.pieceInfo.type
    }

    this.stompService.stompClient!.send(
      '/app/board/move-piece',
      {},
      JSON.stringify(moveRequest)
    )
  }

  sendChessBoardInitializeRequest() {

    let initializeNewBoardRequest = {
      'newBoardId': uuidv4()
    }

    this.stompService.stompClient!.send(
      '/app/board/create',
      {},
      JSON.stringify(initializeNewBoardRequest)
    )
  }

  getPieceByFileAndRank(file: string, rank: number): PieceInfo | undefined {
    if (this.boardView) {

      let square: string = file + rank

      let result: PieceInfo | undefined;

      return result = this.boardView.piecesLocations[square]
    } else {
      return undefined
    }
  }

  updateChessBoard(board: any) {
    this.boardView = { boardId: "", piecesLocations: {}, currentPlayerColor: PieceColor.BLACK }
    this.boardView = JSON.parse(board.body)
  }

  displayInfoWithIllegalMove(illegalMoveView: any) {
    let illegalMoveViewParsed: IllegalMoveView = JSON.parse(illegalMoveView.body)
    this.illegalMoveViewSubject.next(illegalMoveViewParsed)
  }

}

