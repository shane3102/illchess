import { createSelector } from "@ngrx/store";
import { ChessGameState } from "../chess-game.state";
import { BoardAdditionalInfoState } from "./board-additional-info.reducers";
import { state } from "@angular/animations";

export const selectBoard = (state: ChessGameState) => state.boardAdditionalInfoState;

export const boardAdditionalInfoSelector = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.boardAdditionalInfoView
)

export const victoriousPlayerColorSelector = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.boardAdditionalInfoView?.victoriousPlayerColor
)

export const gameStateSelector = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.boardAdditionalInfoView?.gameState
)

export const isWhiteProposingDraw = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.boardAdditionalInfoView.whitePlayer?.isProposingDraw 
)

export const isBlackProposingDraw = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.boardAdditionalInfoView.blackPlayer?.isProposingDraw 
)