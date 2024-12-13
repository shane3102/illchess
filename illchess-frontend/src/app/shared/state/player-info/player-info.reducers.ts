import { createReducer, on } from "@ngrx/store";
import { GameSnippetView } from "../../model/player-info/GameSnippetView";
import { Page } from "../../model/player-info/Page";
import { PlayerView } from "../../model/player-info/PlayerView";
import { changeUsername, commonPageSizeChange, latestGamesLoaded, playerRankingLoaded } from "./player-info.actions";

export interface PlayerInfoState {
    latestGamesInfoPage: Page<GameSnippetView> | null
    playerRankingInfoPage: Page<PlayerView> | null
    pageSize: number,
    username?: string
}

export const initialState: PlayerInfoState = {
    latestGamesInfoPage: null,
    playerRankingInfoPage: null,
    pageSize: 5,
    username: localStorage.getItem('username') ? localStorage.getItem('username')! : undefined
}

export const playerInfoReducer = createReducer(
    initialState,

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
        latestGamesLoaded,
        (state: PlayerInfoState, view: Page<GameSnippetView>) => (
            {
                ...state,
                latestGamesInfoPage: view
            }
        )
    ),

    on(
        commonPageSizeChange,
        (state: PlayerInfoState, dto: { pageSize: number }) => (
            {
                ...state,
                latestGamesInfoPage: null,
                playerRankingInfoPage: null,
                pageSize: dto.pageSize
            }
        )
    ),

    on(
        changeUsername,
        (state: PlayerInfoState, dto: {username: string}) => (
            {
                ...state,
                username: dto.username
            }
        )
    )
)