import { createSelector } from "@ngrx/store";
import { ChessGameState } from "../chess-game.state";
import { ActiveBoardsState } from "./active-boards.reducer";

export const selectActiveBoardsState = (state: ChessGameState) => state.activeBoardsState;

export const selectActiveBoards = createSelector(
    selectActiveBoardsState,
    (state: ActiveBoardsState) => state.activeBoardsView
)