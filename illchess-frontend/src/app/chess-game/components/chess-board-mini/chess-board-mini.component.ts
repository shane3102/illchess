import { Component, Input, OnDestroy, OnInit, Signal, inject } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { BoardGameObtainedInfoView } from '../../../shared/model/game/BoardGameObtainedInfoView';
import { BoardView } from '../../../shared/model/game/BoardView';
import { SquareInfo } from '../../../shared/model/game/SquareInfo';
import { ChessBoardWebsocketService } from '../../../shared/service/GameWebsocketService';
import { removeFinishedBoardFromActiveBoard } from '../../../shared/state/active-boards/active-boards.actions';
import { ChessGameState } from '../../../shared/state/chess-game.state';
import { ChessBoardMiniStore } from './ChessBoardMiniStore';

@Component({
  selector: 'app-chess-board-mini',
  templateUrl: './chess-board-mini.component.html',
  styleUrls: ['./chess-board-mini.component.scss'],
  providers: [ChessBoardMiniStore]
})
export class ChessBoardMiniComponent implements OnInit, OnDestroy {

  @Input() boardId: string

  isBoardFaded: boolean = false

  ranks: number[] = [8, 7, 6, 5, 4, 3, 2, 1]
  files: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

  private chessBoardMiniStore = inject(ChessBoardMiniStore)
  private chessBoardWebSocketService = inject(ChessBoardWebsocketService)
  private store = inject(Store<ChessGameState>)

  boardView: Signal<BoardView | undefined> = this.chessBoardMiniStore.boardView
  boardGameObtainedInfoView$: Observable<BoardGameObtainedInfoView | undefined> = toObservable(this.chessBoardMiniStore.boardGameObtainedInfoView)
  chessTopic$: Subscription
  chessStatus$: Subscription

  ngOnInit(): void {
    this.chessBoardMiniStore.refresh(this.boardId)
    setTimeout(
      async () => {
        this.chessTopic$ = await this.chessBoardWebSocketService.subscribe(
          `/chess-topic/${this.boardId}`,
          (response: any) => {
            let boardView: BoardView = JSON.parse(response)
            this.chessBoardMiniStore.patchBoardPosition(boardView)
          }
        )
        this.chessStatus$ = await this.chessBoardWebSocketService.subscribe(
          `/chess-topic/obtain-status/${this.boardId}`,
          (response: any) => {
            let boardGameObtainedInfoView: BoardGameObtainedInfoView = JSON.parse(response)
            this.chessBoardMiniStore.patchObtainedInfoView(boardGameObtainedInfoView)
          }
        )
      }
    )
    
    this.boardGameObtainedInfoView$.subscribe(
      boardGameObtainedInfoView => {
        if (boardGameObtainedInfoView?.status == 'SUCCESS') {
          this.fadeBoard()
        }
      }
    )
  }

  ngOnDestroy(): void {
    this.chessTopic$.unsubscribe()
    this.chessStatus$.unsubscribe()
  }

  public calculateSquareColor(squareInfo: SquareInfo) {
    return (squareInfo.rank + this.fileToNumber(squareInfo)) % 2 == 0 ? 'rgba(150, 75, 0)' : '#F3E5AB';
  }

  private fadeBoard() {
    this.isBoardFaded = true
    setTimeout(
      () => {
        this.store.dispatch(removeFinishedBoardFromActiveBoard({ boardId: this.boardId }))
      },
      950
    )
  }

  private fileToNumber(squareInfo: SquareInfo): number {
    switch (squareInfo.file) {
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
