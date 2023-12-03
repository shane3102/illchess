import { Component, Input, OnInit } from '@angular/core';
import { Piece, PieceColor, PieceInfo } from '../../model/PieceInfo';
import { PieceDraggedInfo } from '../../model/PieceDraggedInfo';
import { SquareInfo } from '../../model/SquareInfo';
import { PieceDroppedInfo } from '../../model/PieceDroppedInfo';
import { Subject } from 'rxjs';
import { StompService } from '../../service/StompService';
import { BoardView, Square } from '../../model/BoardView';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'app-chess-board',
  templateUrl: './chess-board.component.html',
  styleUrls: ['./chess-board.component.scss']
})
export class ChessBoardComponent implements OnInit {

  boardView: BoardView;

  moveSubject: Subject<PieceDroppedInfo> = new Subject<PieceDroppedInfo>();

  ranks: number[] = [8, 7, 6, 5, 4, 3, 2, 1]
  files: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

  currentllyDraggedPiece: PieceDraggedInfo;

  constructor(
    private stompService: StompService,
  ) {
    stompService.subscribe("/chess-topic", this.updateChessBoard)
  }

  ngOnInit(): void {
    this.stompService.subscribe("/chess-topic", (boardView: BoardView) => this.updateChessBoard(boardView))
    setTimeout(() => this.sendChessBoardInitializeRequest(), 1000)
  }

  pieceDraggedChange(pieceDraggedInfo: PieceDraggedInfo) {
    this.currentllyDraggedPiece = pieceDraggedInfo;
  }

  pieceDroppedChange(pieceDroppedInfo: SquareInfo) {
    // this.moveSubject.next({ pieceInfo: this.currentllyDraggedPiece.pieceInfo, squareFromInfo: this.currentllyDraggedPiece.squareInfo, squareToInfo: pieceDroppedInfo })
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

}

