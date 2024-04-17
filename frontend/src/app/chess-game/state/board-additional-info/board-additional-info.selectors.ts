import { createSelector } from "@ngrx/store";
import { ChessGameState } from "../chess-game.state";
import { BoardAdditionalInfoState } from "./board-additional-info.reducers";

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