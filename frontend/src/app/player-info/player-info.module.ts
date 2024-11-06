import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommonTableComponent } from './components/common-table/common-table.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { PlayerRankingComponent } from './components/player-ranking/player-ranking.component';
import { PlayerViewToTableCellDataPipe } from './pipes/player-view-to-table-cell-data.pipe';



@NgModule({
  declarations: [
    CommonTableComponent,
    PlayerRankingComponent,
    PlayerViewToTableCellDataPipe
  ],
  imports: [
    CommonModule,
    FontAwesomeModule
  ],
  exports: [
    PlayerRankingComponent
  ]
})
export class PlayerInfoModule { }
