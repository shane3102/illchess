import { createSelector } from "@ngrx/store";
import { ChessGameState } from "../chess-game.state";
import { PlayerInfoState } from "./player-info.reducers";
import { state } from "@angular/animations";

export const selectPlayerInfo = (state: ChessGameState) => state.playerInfoState;

export const playerInfoSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state
)

export const playerRankingSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.playerRankingInfoPage?.content
)

export const pageNumberPlayerRankingSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.playerRankingInfoPage ? state.playerRankingInfoPage?.pageNumber : 0
)

export const totalPageNumberPlayerRankingSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.playerRankingInfoPage?.totalPages
)

export const latestGamesSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.latestGamesInfoPage?.content
)

export const pageNumberLatestGamesSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.latestGamesInfoPage ? state.latestGamesInfoPage?.pageNumber : 0
)

export const totalPageNumberLatestGamesSelector = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.latestGamesInfoPage?.totalPages
)

export const commonPageSize = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.pageSize
)

export const username = createSelector(
    selectPlayerInfo,
    (state: PlayerInfoState) => state.username
)