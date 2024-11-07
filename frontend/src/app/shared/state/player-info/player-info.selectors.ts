import { createSelector } from "@ngrx/store";
import { ChessGameState } from "../chess-game.state";
import { PlayerInfoState } from "./player-info.reducers";

export const selectPlayerInfo = (state: ChessGameState) => state.playerInfoState;

export const playerInfoSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state
)

export const currentPageContentPlayerRankingSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.playerRankingInfoPage?.content
)

export const currentPageNumberPlayerRankingSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.playerRankingInfoPage ? state.playerRankingInfoPage?.pageNumber : 0
)

export const totalPageNumberPlayerRankingSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.playerRankingInfoPage?.totalPages
)

export const commonPageSize = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.pageSize
)