import { Component, Input, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, Subject, combineLatest, map } from 'rxjs';
import { GameSnippetView } from 'src/app/shared/model/player-info/GameSnippetView';
import { ChessGameState } from 'src/app/shared/state/chess-game.state';
import { loadLatestGames } from 'src/app/shared/state/player-info/player-info.actions';
import { commonPageSize, latestGamesSelector, pageNumberLatestGamesSelector, totalPageNumberLatestGamesSelector } from 'src/app/shared/state/player-info/player-info.selectors';
import { PageData } from '../../model/PageData';

@Component({
  selector: 'app-latest-games',
  templateUrl: './latest-games.component.html',
  styleUrls: ['./latest-games.component.scss']
})
export class LatestGamesComponent {

  @Input() reloadPage$: Observable<void>

  store = inject(Store<ChessGameState>)

  pageDataLatestGames$: Observable<PageData>
  reloadSubject: Subject<void> = new Subject<void>();

  currentContent$: Observable<GameSnippetView[] | null | undefined> = this.store.select(latestGamesSelector)
  currentPage$: Observable<number> = this.store.select(pageNumberLatestGamesSelector)
  totalPages$: Observable<number | null | undefined> = this.store.select(totalPageNumberLatestGamesSelector)
  commonPageSize$: Observable<number> = this.store.select(commonPageSize)

  ngOnInit(): void {
    this.pageDataLatestGames$ = combineLatest([this.currentPage$, this.commonPageSize$])
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

  reloadLatestGames(event: PageData) {
    this.store.dispatch(
      loadLatestGames(
        {
          pageNumberLatestGames: event.pageNumber,
          pageSizeLatestGames: event.pageSize
        }
      )
    )
  }
}
