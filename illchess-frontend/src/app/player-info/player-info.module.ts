import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommonTableComponent } from './components/common-table/common-table.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { PlayerRankingComponent } from './components/player-ranking/player-ranking.component';
import { PlayerViewToTableCellDataPipe } from './pipes/player-view-to-table-cell-data.pipe';
import { SharedModule } from '../shared/shared.module';
import { LatestGamesToTableCellDataPipe } from './pipes/latest-games-to-table-cell-data.pipe';
import { LatestGamesComponent } from './components/latest-games/latest-games.component';
import { SharedPlayerInfoTablesComponent } from './components/shared-player-info-tables/shared-player-info-tables.component';
import { NumberToArrayPipe } from './pipes/number-to-array.pipe';

@NgModule({
  declarations: [
    CommonTableComponent,
    PlayerRankingComponent,
    PlayerViewToTableCellDataPipe,
    LatestGamesToTableCellDataPipe,
    LatestGamesComponent,
    SharedPlayerInfoTablesComponent,
    NumberToArrayPipe
  ],
  imports: [
    SharedModule,
    CommonModule,
    FontAwesomeModule
  ],
  exports: [
    SharedPlayerInfoTablesComponent
  ]
})
export class PlayerInfoModule { }
