import { Component, Input, OnInit, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, of } from 'rxjs';
import { BestMoveAndContinuationResponse } from '../../model/BestMoveAndContinuationResponse';
import { BoardAdditionalInfoView } from '../../model/BoardAdditionalInfoView';
import { EvaluationResponse } from '../../model/EvaluationResponse';
import { establishBestMoveAndContinuation, establishEvaluation } from '../../state/board-additional-info/board-additional-info.actions';
import { bestMoveAndContinuation, boardAdditionalInfoSelector, evaluation } from '../../state/board-additional-info/board-additional-info.selectors';
import { ChessGameState } from '../../state/chess-game.state';

@Component({
  selector: 'app-chess-engine-info',
  templateUrl: './chess-engine-info.component.html',
  styleUrls: ['./chess-engine-info.component.scss']
})
export class ChessEngineInfoComponent implements OnInit {

  @Input() boardId: string;
  @Input() username: string;
  @Input() boardAdditionalInfoView: BoardAdditionalInfoView | undefined | null;

  store = inject(Store<ChessGameState>)

  boardAdditionalInfoView$: Observable<BoardAdditionalInfoView> = this.store.select(boardAdditionalInfoSelector)
  boardEvaluation$: Observable<EvaluationResponse> = this.store.select(evaluation)
  bestMoveAndContinuation$: Observable<BestMoveAndContinuationResponse> = this.store.select(bestMoveAndContinuation)

  ngOnInit(): void {
    this.boardAdditionalInfoView$.subscribe(
      boardAdditionalInfoView => {
        if (
          boardAdditionalInfoView?.whitePlayer?.username != this.username &&
          boardAdditionalInfoView?.blackPlayer?.username != this.username &&
          boardAdditionalInfoView.boardId
        ) {
          this.store.dispatch(establishEvaluation({ boardId: this.boardId }))
          this.store.dispatch(establishBestMoveAndContinuation({ boardId: this.boardId }))
        }
      }
    )
  }


}
