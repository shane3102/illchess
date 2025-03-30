import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { faCrown, faEquals, faHashtag } from '@fortawesome/free-solid-svg-icons';
import { Observable } from 'rxjs';
import { BoardLegalMovesResponse } from '../../../shared/model/game/BoardLegalMovesResponse';
import { MoveView } from '../../../shared/model/game/BoardView';
import { IllegalMoveResponse } from '../../../shared/model/game/IllegalMoveView';
import { MovePieceRequest } from '../../../shared/model/game/MovePieceRequest';
import { PieceSelectedInfo } from '../../../shared/model/game/PieceSelectedInfo';
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
  @Input() selectedPieceInfo: PieceSelectedInfo | undefined | null;
  @Input() legalMoves: BoardLegalMovesResponse | undefined | null;
  @Input() username: string
  @Input() lastPerformedMove: MoveView | undefined
  @Input() preMoves: MoveView[] | undefined
  @Input() gameState: 'CONTINUE' | 'WHITE_WON' | 'BLACK_WON' | 'DRAW' | null | undefined
  @Input() gameResultCause: 'CHECKMATE' | 'RESIGNATION' | 'STALEMATE' | 'INSUFFICIENT_MATERIAL' | 'PLAYER_AGREEMENT' | null | undefined
  @Input() currentPlayerColor: string | null | undefined
  @Input() userColor: 'WHITE' | 'BLACK' | undefined

  @Output() pieceSelectedInfoEmitter: EventEmitter<PieceSelectedInfo> = new EventEmitter();
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

  pieceSelected() {
    if (this.selectedPieceInfo?.pieceInfo != this.piece || !this.legalMoves) {
      this.pieceSelectedInfoEmitter.emit(new PieceSelectedInfo(<PieceInfo>this.piece, this.squareInfo))
    }
  }

  pieceDraggedRelease() {
    this.pieceDraggedReleasedInfoEmitter.emit()
  }

  squareClicked() {
    if (this.selectedPieceInfo) {
      if (
        (this.selectedPieceInfo?.pieceInfo?.color != this.piece?.color)
        ||
        (this.preMoves != undefined && this.preMoves?.length != 0 && this.selectedPieceInfo.squareInfo != this.squareInfo)
      ) {
        this.pieceDropped()
      } else if (this.piece?.color == this.userColor) {
        this.pieceSelected()
      }
      if (this.selectedPieceInfo?.pieceInfo == this.piece) {
        this.pieceDraggedRelease()
      }
    } else if (this.piece?.color == this.userColor) {
      this.pieceSelected()
    }
  }

  pieceDropped() {
    this.isDraggedOver = false
    if (this.selectedPieceInfo) {
      if (
        (
          this.squareInfo.rank == 8 && this.selectedPieceInfo.pieceInfo.color == PieceColor.WHITE && this.selectedPieceInfo.squareInfo.rank == 7 ||
          this.squareInfo.rank == 1 && this.selectedPieceInfo.pieceInfo.color == PieceColor.BLACK && this.selectedPieceInfo.squareInfo.rank == 2
        )
        && this.selectedPieceInfo.pieceInfo.type == Piece.PAWN
        && (this.preMoves?.length != 0 || this.selectedPieceInfo.pieceInfo.color != this.currentPlayerColor || this.isSquareLegalMove())
      ) {
        this.displayPiecePromotingComponent = true;
      } else {
        let moveRequest: MovePieceRequest = {
          'boardId': this.boardId,
          'startSquare': this.selectedPieceInfo.squareInfo.file + this.selectedPieceInfo.squareInfo.rank,
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
    if (this.selectedPieceInfo && this.legalMoves) {
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
