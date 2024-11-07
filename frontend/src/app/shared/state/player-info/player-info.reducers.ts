import { createReducer, on } from "@ngrx/store";
import { GameSnippetView } from "../../model/player-info/GameSnippetView";
import { Page } from "../../model/player-info/Page";
import { PlayerView } from "../../model/player-info/PlayerView";
import { latestGamesLoaded, nextPageLatestGames, nextPagePlayerRanking, playerRankingLoaded as playerRankingLoaded, previousPageLatestGames, previousPagePlayerRanking } from "./player-info.actions";

export interface PlayerInfoState {
    latestGamesInfoPage?: Page<GameSnippetView>
    playerRankingInfoPage?: Page<PlayerView>
    pageSize: number
}

export const initialState: PlayerInfoState = {
    pageSize: 5
}

export const playerInfoReducer = createReducer(
    initialState,

    on(
        nextPagePlayerRanking,
        (state: PlayerInfoState) => (
            {
                ...state,
                playerRankingInfoPage: undefined
            }
        )
    ),

    on(
        previousPagePlayerRanking,
        (state: PlayerInfoState) => (
            {
                ...state,
                playerRankingInfoPage: undefined
            }
        )
    ),

    on(
        playerRankingLoaded,
        (state: PlayerInfoState, view: Page<PlayerView>) => (
            {
                ...state,
                playerRankingInfoPage: view
            }
        )
    ),

    on(
        nextPageLatestGames,
        (state: PlayerInfoState) => (
            {
                ...state,
                latestGamesInfoPage: undefined
            }
        )
    ),

    on(
        previousPageLatestGames,
        (state: PlayerInfoState) => (
            {
                ...state,
                latestGamesInfoPage: undefined
            }
        )
    ),

    on(
        latestGamesLoaded,
        (state: PlayerInfoState, view: Page<GameSnippetView>) => (
            {
                ...state,
                latestGamesInfoPage: view
            }
        )
    )
)