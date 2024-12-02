import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Store } from '@ngrx/store';
import { ChessGameState } from '../../../shared/state/chess-game.state';
import { BoardAdditionalInfoView } from '../../../shared/model/game/BoardAdditionalInfoView';
import { Observable } from 'rxjs';
import { boardAdditionalInfoSelector } from '../../../shared/state/board-additional-info/board-additional-info.selectors';
import { BoardView, PiecesLocations } from 'src/app/shared/model/game/BoardView';
import { boardSelector } from 'src/app/shared/state/board/board.selectors';
import { RefreshBoardDto } from 'src/app/shared/model/game/RefreshBoardRequest';
import { refreshBoard } from 'src/app/shared/state/board/board.actions';

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
  boardView$: Observable<BoardView> = this.store.select(boardSelector)

  ngOnInit(): void {
    this.route.params.subscribe(
      params => {
        this.boardId = params['boardId']
        this.username =  params['username']
      }
    )
    this.sendChessBoardRefreshRequest()
  }

  sendChessBoardRefreshRequest() {
    let refreshBoardDto: RefreshBoardDto = {
      'boardId': this.boardId
    }
    this.store.dispatch(refreshBoard(refreshBoardDto))
  }

  haveAnyPiecesSet(piecesLocations: PiecesLocations) {
    return Object.keys(piecesLocations).length != 0
  }

}
