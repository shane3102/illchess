import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SpinnerComponent } from './components/spinner/spinner.component';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { boardReducer } from './state/board/board.reducer';
import { activeBoardsReducer } from './state/active-boards/active-boards.reducer';
import { boardAdditionalInfoReducer } from './state/board-additional-info/board-additional-info.reducers';
import { BoardEffects } from './state/board/board.effects';
import { ActiveBoardEffects } from './state/active-boards/active-boards.effects';
import { BoardAdditionalInfoEffects } from './state/board-additional-info/board-additional-info.effects';

@NgModule({
  declarations: [
    SpinnerComponent
  ],
  imports: [
    CommonModule,
    StoreModule.forRoot(
      {
        boardState: boardReducer,
        activeBoardsState: activeBoardsReducer,
        boardAdditionalInfoState: boardAdditionalInfoReducer
      }
    ),
    EffectsModule.forRoot([BoardEffects, ActiveBoardEffects, BoardAdditionalInfoEffects])
  ],
  exports: [
    SpinnerComponent
  ]
})
export class SharedModule { }
