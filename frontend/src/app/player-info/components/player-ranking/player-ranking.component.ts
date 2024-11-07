import { Component, OnInit, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, combineLatest, map } from 'rxjs';
import { PlayerView } from 'src/app/shared/model/player-info/PlayerView';
import { ChessGameState } from 'src/app/shared/state/chess-game.state';
import { loadPlayerRanking } from 'src/app/shared/state/player-info/player-info.actions';
import { commonPageSize, pageNumberPlayerRankingSelector, playerRankingSelector, totalPageNumberPlayerRankingSelector } from 'src/app/shared/state/player-info/player-info.selectors';
import { PageData } from '../../model/PageData';

@Component({
  selector: 'app-player-ranking',
  templateUrl: './player-ranking.component.html',
  styleUrls: ['./player-ranking.component.scss']
})
export class PlayerRankingComponent implements OnInit {

  store = inject(Store<ChessGameState>)

  data$: Observable<PageData>

  currentContent$: Observable<PlayerView[] | null | undefined> = this.store.select(playerRankingSelector)
  currentPage$: Observable<number> = this.store.select(pageNumberPlayerRankingSelector)
  totalPages$: Observable<number | null | undefined> = this.store.select(totalPageNumberPlayerRankingSelector)
  commonPageSize$: Observable<number> = this.store.select(commonPageSize)

  ngOnInit(): void {
    this.data$ = combineLatest([this.currentPage$, this.commonPageSize$])
      .pipe(
        map(
          ([pageNumber, pageSize]) => {
            return {
              pageNumber,
              pageSize
            }
          }
        )
      )
  }

  reloadRanking(event: { pageNumber: number, pageSize: number }) {
    this.store.dispatch(
      loadPlayerRanking(
        {
          pageNumberPlayerRanking: event.pageNumber,
          pageSizePlayerRanking: event.pageSize
        }
      )
    )
  }

}
