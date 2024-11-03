import { createSelector } from "@ngrx/store";
import { ChessGameState } from "../chess-game.state";
import { PlayerInfoState } from "./player-info.reducers";

export const selectPlayerInfo = (state: ChessGameState) => state.playerInfoState;

export const playerInfoSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state
)