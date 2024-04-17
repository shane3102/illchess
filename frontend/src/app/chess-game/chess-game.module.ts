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
import { ChessPromotingPieceComponent } from './components/chess-promoting-piece/chess-promoting-piece.component';
import { ChessLegalMoveCircleComponent } from './components/chess-legal-move-circle/chess-legal-move-circle.component';
import { JoinOrInitializeGameComponent } from './components/join-or-initialize-game/join-or-initialize-game.component';
import { ChessBoardMiniComponent } from './components/chess-board-mini/chess-board-mini.component';
import { ChessPieceMiniComponent } from './components/chess-piece-mini/chess-piece-mini.component';
import { ActiveBoardsComponent } from './components/active-boards/active-boards.component';
import { activeBoardsReducer } from './state/active-boards/active-boards.reducer';
import { ActiveBoardEffects } from './state/active-boards/active-boards.effects';
import { SharedModule } from '../shared/shared.module';
import { BoardAdditionalInfoEffects } from './state/board-additional-info/board-additional-info.effects';
import { boardAdditionalInfoReducer } from './state/board-additional-info/board-additional-info.reducers';
import { ChessBoardAdditionalInfoComponent } from './components/chess-board-additional-info/chess-board-additional-info.component';
import { ChessGameComponent } from './components/chess-game/chess-game.component';
import { CapturedPiecesComponent } from './components/captured-pieces/captured-pieces.component';
import { KingBoardInfoAfterGameFinishedComponent } from './components/king-board-info-after-game-finished/king-board-info-after-game-finished.component';


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
    ChessSquareComponent,
    ChessPromotingPieceComponent,
    ChessLegalMoveCircleComponent,
    JoinOrInitializeGameComponent,
    ChessBoardMiniComponent,
    ChessPieceMiniComponent,
    ActiveBoardsComponent,
    ChessBoardAdditionalInfoComponent,
    ChessGameComponent,
    CapturedPiecesComponent,
    KingBoardInfoAfterGameFinishedComponent
  ],
  imports: [
    SharedModule,
    CommonModule,
    FontAwesomeModule,
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
    ChessGameComponent,
    JoinOrInitializeGameComponent,
    ActiveBoardsComponent
  ]
})
export class ChessGameModule { }
