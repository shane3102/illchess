import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Piece, PieceInfo } from '../../model/PieceInfo';
import { PieceDraggedInfo } from '../../model/PieceDraggedInfo';
import { SquareInfo } from '../../model/SquareInfo';
import { Observable } from 'rxjs';
import { IllegalMoveResponse } from '../../model/IllegalMoveView';
import { MovePieceRequest } from '../../model/MovePieceRequest';
import { BoardLegalMovesResponse } from '../../model/BoardLegalMovesResponse';
import { MoveView } from '../../model/BoardView';
import { IconDefinition, faCrown, faHashtag, faEquals } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-chess-square',
  templateUrl: './chess-square.component.html',
  styleUrls: ['./chess-square.component.scss']
})
export class ChessSquareComponent implements OnInit {

  @Input() boardId: string;
  @Input() piece: PieceInfo | undefined;
  @Input() squareInfo: SquareInfo;
  @Input() illegalMoveResponse: Observable<IllegalMoveResponse>
  @Input() draggedPieceInfo: PieceDraggedInfo | undefined | null;
  @Input() legalMoves: BoardLegalMovesResponse | undefined | null;
  @Input() username: string
  @Input() lastPerformedMove: MoveView | undefined
  @Input() gameState: 'CONTINUE' | 'CHECKMATE' | 'STALEMATE' | null | undefined
  @Input() victoriousPlayerColor: string | null | undefined

  @Output() pieceDraggedInfoEmitter: EventEmitter<PieceDraggedInfo> = new EventEmitter();
  @Output() pieceDraggedReleasedInfoEmitter: EventEmitter<void> = new EventEmitter();
  @Output() pieceDroppedInfoEmitter: EventEmitter<MovePieceRequest> = new EventEmitter();

  isDraggedOver: boolean = false;
  illegalMove: boolean = false;
  displayPiecePromotingComponent: boolean = false;

  crownIcon = faCrown
  hashtag = faHashtag
  equals = faEquals

  constructor() { }

  ngOnInit(): void {

    this.illegalMoveResponse.subscribe(
      illegalMoveView => {
        this.isDraggedOver = false;
        if (illegalMoveView.highlightSquare == this.squareInfo.file + this.squareInfo.rank) {
          this.displayIllegalMoveAnimation()
        }
      }
    )
  }

  public calculateSquareColor(): string {
    return (this.squareInfo.rank + this.fileToNumber()) % 2 == 0 ? 'rgba(150, 75, 0)' : '#F3E5AB';
  }

  public displayIllegalMoveAnimation() {
    this.illegalMove = true
    setTimeout(() => this.illegalMove = false, 2000)
  }

  pieceDragged() {
    if (this.draggedPieceInfo?.pieceInfo != this.piece || !this.legalMoves) {
      this.pieceDraggedInfoEmitter.emit(new PieceDraggedInfo(<PieceInfo>this.piece, this.squareInfo))
    }
  }

  pieceDraggedRelease() {
    this.pieceDraggedReleasedInfoEmitter.emit()
  }

  pieceDropped() {
    this.isDraggedOver = false
    if (this.draggedPieceInfo) {
      if ((this.squareInfo.rank == 8 || this.squareInfo.rank == 1) && this.draggedPieceInfo.pieceInfo.type == Piece.PAWN && this.isSquareLegalMove()) {
        this.displayPiecePromotingComponent = true;
      } else {
        let moveRequest: MovePieceRequest = {
          'boardId': this.boardId,
          'startSquare': this.draggedPieceInfo.squareInfo.file + this.draggedPieceInfo.squareInfo.rank,
          'targetSquare': this.squareInfo.file + this.squareInfo.rank,
          'pieceColor': this.draggedPieceInfo.pieceInfo.color,
          'pieceType': this.draggedPieceInfo.pieceInfo.type,
          'username': this.username
        }
        this.pieceDroppedInfoEmitter.emit(moveRequest)
      }
    }
  }

  piecePromotion(moveRequest: MovePieceRequest) {
    this.displayPiecePromotingComponent = false;
    this.isDraggedOver = false;
    this.pieceDroppedInfoEmitter.emit(moveRequest);
  }

  pieceDraggedOver(event: any) {
    event.preventDefault()

    this.isDraggedOver = true;
  }

  isSquareLegalMove(): boolean {
    if (this.draggedPieceInfo && this.legalMoves) {
      return this.legalMoves.legalSquares.some(it => it.toString() == this.squareInfo.file + this.squareInfo.rank)
    }
    return false
  }

  isLastPerformedMove(): boolean {

    let currentSquare: string = this.squareInfo.file + this.squareInfo.rank

    return currentSquare == this.lastPerformedMove?.startSquare || currentSquare == this.lastPerformedMove?.targetSquare
  }

  

  private fileToNumber(): number {
    switch (this.squareInfo.file) {
      case 'A':
        return 1;
      case 'B':
        return 2;
      case 'C':
        return 3;
      case 'D':
        return 4;
      case 'E':
        return 5;
      case 'F':
        return 6;
      case 'G':
        return 7;
      case 'H':
        return 8;
    }
    return 0;
  }

}
