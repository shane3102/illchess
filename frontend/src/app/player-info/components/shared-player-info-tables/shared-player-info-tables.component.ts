import { Component, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { ChessGameState } from 'src/app/shared/state/chess-game.state';
import { commonPageSizeChange } from 'src/app/shared/state/player-info/player-info.actions';
import { commonPageSize } from 'src/app/shared/state/player-info/player-info.selectors';

@Component({
  selector: 'app-shared-player-info-tables',
  templateUrl: './shared-player-info-tables.component.html',
  styleUrls: ['./shared-player-info-tables.component.scss']
})
export class SharedPlayerInfoTablesComponent {
  
  store = inject(Store<ChessGameState>)

  commonPageSize$: Observable<number | undefined | null> = this.store.select(commonPageSize)

  availablePageSizes = [
    5,
    10,
    25,
    100
  ]

  changePageSize(newPageSize: number) {
    this.store.dispatch(commonPageSizeChange({pageSize: newPageSize}))
  }

}
