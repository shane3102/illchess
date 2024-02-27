import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChessBoardComponent } from './components/chess-board/chess-board.component';
import { ChessPieceComponent } from './components/chess-piece/chess-piece.component';
import { PawnComponent } from './components/pieces/pawn/pawn.component';
import { KnightComponent } from './components/pieces/knight/knight.component';
import { BishopComponent } from './components/pieces/bishop/bishop.component';
import { RookComponent } from './components/pieces/rook/rook.component';
import { QueenComponent } from './components/pieces/queen/queen.component';
import { KingComponent } from './components/pieces/king/king.component';
import { ChessSquareComponent } from './components/chess-square/chess-square.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { StoreModule } from '@ngrx/store';
import { boardReducer } from './state/board/board.reducer';
import { EffectsModule } from '@ngrx/effects';
import { BoardEffects } from './state/board/board.effects';


@NgModule({
  declarations: [
    ChessBoardComponent,
    ChessPieceComponent,
    PawnComponent,
    KnightComponent,
    BishopComponent,
    RookComponent,
    QueenComponent,
    KingComponent,
    ChessSquareComponent
  ],
  imports: [
    CommonModule,
    FontAwesomeModule,
    StoreModule.forRoot({boardState: boardReducer}),
    EffectsModule.forRoot([BoardEffects])
  ],
  exports:[
    ChessBoardComponent
  ]
})
export class ChessGameModule { }
