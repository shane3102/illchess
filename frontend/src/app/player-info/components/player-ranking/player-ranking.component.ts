import { Component, OnInit, inject } from '@angular/core';
import { faExclamationCircle } from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { Observable, combineLatest, map } from 'rxjs';
import { PlayerView } from 'src/app/shared/model/player-info/PlayerView';
import { ChessGameState } from 'src/app/shared/state/chess-game.state';
import { loadPlayerRanking, nextPagePlayerRanking, previousPagePlayerRanking } from 'src/app/shared/state/player-info/player-info.actions';
import { commonPageSize, playerRankingSelector, pageNumberPlayerRankingSelector, totalPageNumberPlayerRankingSelector } from 'src/app/shared/state/player-info/player-info.selectors';

@Component({
  selector: 'app-player-ranking',
  templateUrl: './player-ranking.component.html',
  styleUrls: ['./player-ranking.component.scss']
})
export class PlayerRankingComponent implements OnInit {

  store = inject(Store<ChessGameState>)

  exclamationCircle = faExclamationCircle

  data$: Observable<PageData>

  currentContent$: Observable<PlayerView[] | null | undefined> = this.store.select(playerRankingSelector)
  currentPage$: Observable<number> = this.store.select(pageNumberPlayerRankingSelector)
  totalPages$: Observable<number | null | undefined> = this.store.select(totalPageNumberPlayerRankingSelector)
  commonPageSize$: Observable<number> = this.store.select(commonPageSize)

  ngOnInit(): void {
    this.data$ = combineLatest([this.currentPage$, this.commonPageSize$])
    .pipe(
      map(
        ([currentPage, pageSize]) => {
          return {
            currentPage,
            pageSize
          }
        }
      )
    )
  }

  nextPage(event: { pageNumber: number, pageSize: number, totalPages: number }) {
    this.store.dispatch(nextPagePlayerRanking(event))
  }

  previousPage(event: { pageNumber: number, pageSize: number, totalPages: number }) {
    this.store.dispatch(previousPagePlayerRanking(event))
  }

  reloadRanking(event: { pageNumber: number, pageSize: number}) {
    this.store.dispatch(loadPlayerRanking(event))
  }

}

interface PageData {
  currentPage: number
  pageSize: number
}
