import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { SharedModule } from '../shared/shared.module';
import { ActiveBoardsComponent } from './components/active-boards/active-boards.component';
import { CapturedPiecesComponent } from './components/captured-pieces/captured-pieces.component';
import { ChessBoardAdditionalInfoComponent } from './components/chess-board-additional-info/chess-board-additional-info.component';
import { ChessBoardMiniComponent } from './components/chess-board-mini/chess-board-mini.component';
import { ChessBoardComponent } from './components/chess-board/chess-board.component';
import { ChessEngineInfoComponent } from './components/chess-engine-info/chess-engine-info.component';
import { ChessGameActionButtonsComponent } from './components/chess-game-action-buttons/chess-game-action-buttons.component';
import { ChessGameButtonComponent } from './components/chess-game-button/chess-game-button.component';
import { ChessGameOfferFromOponentComponent } from './components/chess-game-offer-from-oponent/chess-game-offer-from-oponent.component';
import { ChessGameComponent } from './components/chess-game/chess-game.component';
import { ChessLegalMoveCircleComponent } from './components/chess-legal-move-circle/chess-legal-move-circle.component';
import { ChessPieceMiniComponent } from './components/chess-piece-mini/chess-piece-mini.component';
import { ChessPieceComponent } from './components/chess-piece/chess-piece.component';
import { ChessPromotingPieceComponent } from './components/chess-promoting-piece/chess-promoting-piece.component';
import { ChessSquareComponent } from './components/chess-square/chess-square.component';
import { GameFinishedPopupUserInfoComponent } from './components/game-finished-popup-user-info/game-finished-popup-user-info.component';
import { GameFinishedPopupComponent } from './components/game-finished-popup/game-finished-popup.component';
import { JoinOrInitializeGameComponent } from './components/join-or-initialize-game/join-or-initialize-game.component';
import { KingBoardInfoAfterGameFinishedComponent } from './components/king-board-info-after-game-finished/king-board-info-after-game-finished.component';
import { BishopComponent } from './components/pieces/bishop/bishop.component';
import { KingComponent } from './components/pieces/king/king.component';
import { KnightComponent } from './components/pieces/knight/knight.component';
import { PawnComponent } from './components/pieces/pawn/pawn.component';
import { QueenComponent } from './components/pieces/queen/queen.component';
import { RookComponent } from './components/pieces/rook/rook.component';
import { LoadingGameComponent } from './components/loading-game/loading-game.component';
import { WaitingForBlackPopupComponent } from './components/waiting-for-black-popup/waiting-for-black-popup.component';


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
    KingBoardInfoAfterGameFinishedComponent,
    ChessGameButtonComponent,
    ChessGameActionButtonsComponent,
    ChessGameOfferFromOponentComponent,
    ChessEngineInfoComponent,
    GameFinishedPopupComponent,
    GameFinishedPopupUserInfoComponent,
    LoadingGameComponent,
    WaitingForBlackPopupComponent
  ],
  imports: [
    SharedModule,
    CommonModule,
    FontAwesomeModule
  ],
  exports: [
    ChessGameComponent,
    JoinOrInitializeGameComponent,
    ActiveBoardsComponent
  ]
})
export class ChessGameModule { }
