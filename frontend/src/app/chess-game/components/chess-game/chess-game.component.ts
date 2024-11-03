import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Store } from '@ngrx/store';
import { ChessGameState } from '../../../shared/state/chess-game.state';
import { BoardAdditionalInfoView } from '../../../shared/model/BoardAdditionalInfoView';
import { Observable } from 'rxjs';
import { boardAdditionalInfoSelector } from '../../../shared/state/board-additional-info/board-additional-info.selectors';

@Component({
  selector: 'app-chess-game',
  templateUrl: './chess-game.component.html',
  styleUrls: ['./chess-game.component.scss']
})
export class ChessGameComponent implements OnInit {

  boardId: string
  username: string

  private store = inject(Store<ChessGameState>)
  private route = inject(ActivatedRoute)

  boardAdditionalInfoView$: Observable<BoardAdditionalInfoView> = this.store.select(boardAdditionalInfoSelector)

  ngOnInit(): void {
    this.route.params.subscribe(
      params => {
        this.boardId = params['boardId']
        this.username =  params['username']
      }
    )
  }
}
