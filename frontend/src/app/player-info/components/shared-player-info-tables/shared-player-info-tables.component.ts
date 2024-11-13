import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, Subject, Subscription } from 'rxjs';
import { ChessBoardWebsocketService } from 'src/app/shared/service/GameWebsocketService';
import { ChessGameState } from 'src/app/shared/state/chess-game.state';
import { commonPageSizeChange } from 'src/app/shared/state/player-info/player-info.actions';
import { commonPageSize } from 'src/app/shared/state/player-info/player-info.selectors';

@Component({
  selector: 'app-shared-player-info-tables',
  templateUrl: './shared-player-info-tables.component.html',
  styleUrls: ['./shared-player-info-tables.component.scss']
})
export class SharedPlayerInfoTablesComponent implements OnInit, OnDestroy  {
  
  store = inject(Store<ChessGameState>)
  chessBoardWebSocketService = inject(ChessBoardWebsocketService)
  
  reloadSubject: Subject<void> = new Subject<void>();

  commonPageSize$: Observable<number | undefined | null> = this.store.select(commonPageSize)

  private obtainStatusSubscription$: Subscription

  availablePageSizes = [
    5,
    10,
    25,
    100
  ]

  changePageSize(newPageSize: number) {
    this.store.dispatch(commonPageSizeChange({pageSize: newPageSize}))
  }

  ngOnInit(): void {
    
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

}
