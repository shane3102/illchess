import { Component, Input, OnInit } from '@angular/core';
import { SquareInfo } from '../../model/SquareInfo';
import { ChessBoardMiniStore } from './chess-board-mini.store';
import { Observable } from 'rxjs';
import { BoardView } from '../../model/BoardView';
import { ChessBoardWebsocketService } from '../../service/ChessBoardWebsocketService';

@Component({
  selector: 'app-chess-board-mini',
  templateUrl: './chess-board-mini.component.html',
  styleUrls: ['./chess-board-mini.component.scss'],
  providers: [ChessBoardMiniStore]
})
export class ChessBoardMiniComponent implements OnInit {

  @Input() boardId: string

  ranks: number[] = [8, 7, 6, 5, 4, 3, 2, 1]
  files: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

  boardView$: Observable<BoardView> = this.chessBoardMiniStore.boardView$

  constructor(
    private chessBoardMiniStore: ChessBoardMiniStore,
    private chessBoardWebSocketService: ChessBoardWebsocketService
  ) {

  }

  ngOnInit(): void {
    this.chessBoardMiniStore.refresh(this.boardId)
    this.chessBoardWebSocketService.subscribe(
      `/chess-topic/${this.boardId}`,
      (response: any) => {
        let boardView: BoardView = JSON.parse(response)
        this.chessBoardMiniStore.setState(boardView)
      }
    )
  }

  public calculateSquareColor(squareInfo: SquareInfo) {
    return (squareInfo.rank + this.fileToNumber(squareInfo)) % 2 == 0 ? 'rgba(150, 75, 0)' : '#F3E5AB';
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
