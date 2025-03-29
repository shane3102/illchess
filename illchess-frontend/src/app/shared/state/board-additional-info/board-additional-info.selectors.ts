import { createSelector } from "@ngrx/store";
import { ChessGameState } from "../chess-game.state";
import { BoardAdditionalInfoState } from "./board-additional-info.reducers";

export const selectBoard = (state: ChessGameState) => state.boardAdditionalInfoState;

export const boardAdditionalInfoSelector = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.boardAdditionalInfoView
)

export const gameResultCause = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.boardAdditionalInfoView?.gameResultCause
)

export const gameStateSelector = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.boardAdditionalInfoView?.gameState
)

export const currentPlayerColorSelector = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.boardAdditionalInfoView?.currentPlayerColor
)

export const isWhiteProposingDraw = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.boardAdditionalInfoView.whitePlayer?.isProposingDraw 
)

export const isBlackProposingDraw = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.boardAdditionalInfoView.blackPlayer?.isProposingDraw 
)

export const evaluation = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.evaluation
)

export const bestMoveAndContinuation = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.bestMoveAndContinuation
)