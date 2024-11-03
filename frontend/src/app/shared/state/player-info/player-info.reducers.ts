import { createReducer, on } from "@ngrx/store";
import { GameSnippetView } from "../../model/player-info/GameSnippetView";
import { currentPagePlayerRankingLoaded, loadCurrentPagePlayerRanking, nextPagePlayerRanking, nextPagePlayerRankingLoaded, previousPagePlayerRanking, previousPagePlayerRankingLoaded } from "./player-info.actions";
import { Page } from "../../model/player-info/Page";
import { PlayerView } from "../../model/player-info/PlayerView";

export interface PlayerInfoState {
    latestGamesInfo: {
        latestGamesPreviousPage?: GameSnippetView[],
        latestGames?: GameSnippetView[]
        latestGamesNextPage?: GameSnippetView[],
        pageNumber: number,
        totalPages?: number
    }
    playerRankingInfo: {
        playerRankingPreviousPage?: PlayerView[]
        playerRanking?: PlayerView[]
        playerRankingNextPage?: PlayerView[]
        pageNumber: number,
        totalPages?: number
    }
    pageSize: number
}

export const initialState: PlayerInfoState = {
    latestGamesInfo: {
        pageNumber: 0
    },
    playerRankingInfo: {
        pageNumber: 0
    },
    pageSize: 5
}

export const playerInfoReducer = createReducer(
    initialState,
    on(
        nextPagePlayerRanking,
        (state: PlayerInfoState) => (
            {
                ...state,
                playerRankingInfo: {
                    ...state.playerRankingInfo,
                    pageNumber: state.playerRankingInfo.pageNumber + 1,
                    playerRankingPreviousPage: state.playerRankingInfo.playerRanking,
                    playerRanking: state.playerRankingInfo.playerRankingNextPage,
                    playerRankingNextPage: []
                }
            }
        )
    ),
    on(
        previousPagePlayerRanking,
        (state: PlayerInfoState) => (
            {
                ...state,
                playerRankingInfo: {
                    ...state.playerRankingInfo,
                    pageNumber: state.playerRankingInfo.pageNumber - 1,
                    playerRankingPreviousPage: [],
                    playerRanking: state.playerRankingInfo.playerRankingPreviousPage,
                    playerRankingNextPage: state.playerRankingInfo.playerRanking
                }
            }
        )
    ),
    on(
        currentPagePlayerRankingLoaded,
        (state: PlayerInfoState, view: Page<PlayerView>) => (
            {
                ...state,
                playerRankingInfo: {
                    ...state.playerRankingInfo,
                    playerRanking: view.content,
                    pageNumber: view.pageNumber,
                    totalPages: view.totalPages
                }
            }
        )
    ),
    on(
        nextPagePlayerRankingLoaded,
        (state: PlayerInfoState, view: Page<PlayerView>) => (
            {
                ...state,
                playerRankingInfo: {
                    ...state.playerRankingInfo,
                    playerRankingNextPage: view.content,
                    totalPages: view.totalPages
                }
            }
        )
    ),
    on(
        previousPagePlayerRankingLoaded,
        (state: PlayerInfoState, view: Page<PlayerView>) => (
            {
                ...state,
                playerRankingInfo: {
                    ...state.playerRankingInfo,
                    playerRankingPreviousPage: view.content,
                    totalPages: view.totalPages
                }
            }
        )
    )
)