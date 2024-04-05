import { createSelector } from "@ngrx/store";
import { ChessGameState } from "../chess-game.state";
import { BoardAdditionalInfoState } from "./board-additional-info.reducers";

export const selectBoard = (state: ChessGameState) => state.boardAdditionalInfoState;

export const boardAdditionalInfoSelector = createSelector(
    selectBoard,
    (state: BoardAdditionalInfoState) => state.boardAdditionalInfoView
)