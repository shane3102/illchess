import { createAction, props } from "@ngrx/store";
import { GameSnippetView } from "../../model/player-info/GameSnippetView";
import { Page } from "../../model/player-info/Page";
import { PlayerView } from "../../model/player-info/PlayerView";

// Player ranking
export const nextPagePlayerRanking = createAction(
    'Next page in player ranking',
    props<{ pageNumber: number, pageSize: number }>()
)

export const previousPagePlayerRanking = createAction(
    'Previous page in player ranking',
    props<{ pageNumber: number, pageSize: number }>()
)

export const loadPlayerRanking = createAction(
    'Load page of player ranking',
    props<{pageNumber: number, pageSize: number }>()
)

export const playerRankingLoaded = createAction(
    'Current page of player ranking loaded',
    props<Page<PlayerView>>()
)

// Latest games - TODO

export const nextPageLatestGames = createAction(
    'Next page in player ranking',
    props<{ pageNumber: number, pageSize: number }>()
)

export const previousPageLatestGames = createAction(
    'Previous page in player ranking',
    props<{ pageNumber: number, pageSize: number }>()
)

export const loadLatestGames = createAction(
    'Reload full latest games',
    props<{ pageNumber: number, pageSize: number }>()
)

export const latestGamesLoaded = createAction(
    'Latest games loaded',
    props<Page<GameSnippetView>>()
)

// Common - TODO
export const commonPageSizeChange = createAction(
    'Common page size change',
    props<{ pageSize: number }>()
)