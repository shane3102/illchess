import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { faCrown, faEquals, faHashtag } from '@fortawesome/free-solid-svg-icons';
import { Observable } from 'rxjs';
import { BoardLegalMovesResponse } from '../../../shared/model/game/BoardLegalMovesResponse';
import { MoveView } from '../../../shared/model/game/BoardView';
import { IllegalMoveResponse } from '../../../shared/model/game/IllegalMoveView';
import { MovePieceRequest } from '../../../shared/model/game/MovePieceRequest';
import { PieceDraggedInfo } from '../../../shared/model/game/PieceDraggedInfo';
import { Piece, PieceColor, PieceInfo } from '../../../shared/model/game/PieceInfo';
import { SquareInfo } from '../../../shared/model/game/SquareInfo';

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
  @Input() preMoves: MoveView[] | undefined
  @Input() gameState: 'CONTINUE' | 'CHECKMATE' | 'STALEMATE' | 'RESIGNED' | 'DRAW' | null | undefined
  @Input() victoriousPlayerColor: string | null | undefined
  @Input() currentPlayerColor: string | null | undefined

  @Output() pieceDraggedInfoEmitter: EventEmitter<PieceDraggedInfo> = new EventEmitter();
  @Output() pieceDraggedReleasedInfoEmitter: EventEmitter<void> = new EventEmitter();
  @Output() pieceDroppedInfoEmitter: EventEmitter<MovePieceRequest> = new EventEmitter();

  isDraggedOver: boolean = false;
  illegalMove: boolean = false;
  displayPiecePromotingComponent: boolean = false;

  crownIcon = faCrown
  hashtag = faHashtag
  equals = faEquals

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
      if (
        (
          this.squareInfo.rank == 8 && this.draggedPieceInfo.pieceInfo.color == PieceColor.WHITE && this.draggedPieceInfo.squareInfo.rank == 7 || 
          this.squareInfo.rank == 1 && this.draggedPieceInfo.pieceInfo.color == PieceColor.BLACK && this.draggedPieceInfo.squareInfo.rank == 2
        )
        && this.draggedPieceInfo.pieceInfo.type == Piece.PAWN
        && (this.preMoves?.length != 0 || this.draggedPieceInfo.pieceInfo.color != this.currentPlayerColor || this.isSquareLegalMove())
      ) {
        this.displayPiecePromotingComponent = true;
      } else {
        let moveRequest: MovePieceRequest = {
          'boardId': this.boardId,
          'startSquare': this.draggedPieceInfo.squareInfo.file + this.draggedPieceInfo.squareInfo.rank,
          'targetSquare': this.squareInfo.file + this.squareInfo.rank,
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

  isPreMoveSquare(): boolean {
    let currentSquare: string = this.squareInfo.file + this.squareInfo.rank

    return this.preMoves?.find(preMove => preMove.startSquare == currentSquare || preMove.targetSquare == currentSquare) != undefined
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
