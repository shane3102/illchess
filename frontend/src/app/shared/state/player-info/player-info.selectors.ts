import { createSelector } from "@ngrx/store";
import { ChessGameState } from "../chess-game.state";
import { PlayerInfoState } from "./player-info.reducers";
import { state } from "@angular/animations";

export const selectPlayerInfo = (state: ChessGameState) => state.playerInfoState;

export const playerInfoSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state
)

export const currentPageContentPlayerRankingSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.playerRankingInfo.playerRanking
)

export const currentPageNumberPlayerRankingSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.playerRankingInfo.pageNumber
)

export const totalPageNumberPlayerRankingSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.playerRankingInfo.totalPages
)

export const commonPageSize = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.pageSize
)