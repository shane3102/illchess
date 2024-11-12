import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, Subject, Subscription, combineLatest, map } from 'rxjs';
import { PlayerView } from 'src/app/shared/model/player-info/PlayerView';
import { ChessGameState } from 'src/app/shared/state/chess-game.state';
import { loadPlayerRanking } from 'src/app/shared/state/player-info/player-info.actions';
import { commonPageSize, pageNumberPlayerRankingSelector, playerRankingSelector, totalPageNumberPlayerRankingSelector } from 'src/app/shared/state/player-info/player-info.selectors';
import { PageData } from '../../model/PageData';
import { ChessBoardWebsocketService } from 'src/app/shared/service/GameWebsocketService';

@Component({
  selector: 'app-player-ranking',
  templateUrl: './player-ranking.component.html',
  styleUrls: ['./player-ranking.component.scss']
})
export class PlayerRankingComponent implements OnInit, OnDestroy {

  store = inject(Store<ChessGameState>)
  chessBoardWebSocketService = inject(ChessBoardWebsocketService)

  data$: Observable<PageData>
  reloadSubject: Subject<void> = new Subject<void>();

  currentContent$: Observable<PlayerView[] | null | undefined> = this.store.select(playerRankingSelector)
  currentPage$: Observable<number> = this.store.select(pageNumberPlayerRankingSelector)
  totalPages$: Observable<number | null | undefined> = this.store.select(totalPageNumberPlayerRankingSelector)
  commonPageSize$: Observable<number> = this.store.select(commonPageSize)

  private obtainStatusSubscription$: Subscription

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
    
    setTimeout(
      async () => {
        this.obtainStatusSubscription$ = await this.chessBoardWebSocketService.subscribe(
          `/chess-topic/obtain-status`,
          () => this.reloadSubject.next()
        )
      }
    )
    
  }

  ngOnDestroy(): void {
    this.obtainStatusSubscription$.unsubscribe()
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
