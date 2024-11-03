import { createAction, props } from "@ngrx/store";
import { PlayerView } from "../../model/player-info/PlayerView";
import { Page } from "../../model/player-info/Page";
import { GameSnippetView } from "../../model/player-info/GameSnippetView";

// Player ranking
export const nextPagePlayerRanking = createAction(
    'Next page in player ranking',
    props<{ pageNumber: number, pageSize: number, totalPages: number }>()
)

export const previousPagePlayerRanking = createAction(
    'Previous page in player ranking',
    props<{ pageNumber: number, pageSize: number, totalPages: number }>()
)

export const loadPreviousPagePlayerRanking = createAction(
    'Load previous page of player ranking',
    props<{previousPageNumber: number, pageSize: number}>()
)

export const loadCurrentPagePlayerRanking = createAction(
    'Load current page of player ranking',
    props<{currentPageNumber: number, pageSize: number}>()
)

export const loadNextPagePlayerRanking = createAction(
    'Load next page of player ranking',
    props<{nextPageNumber: number, pageSize: number}>()
)

export const previousPagePlayerRankingLoaded = createAction(
    'Previous page of player ranking loaded',
    props<Page<PlayerView>>()
)

export const currentPagePlayerRankingLoaded = createAction(
    'Current page of player ranking loaded',
    props<Page<PlayerView>>()
)

export const nextPagePlayerRankingLoaded = createAction(
    'Next page of player ranking loaded',
    props<Page<PlayerView>>()
)

export const reloadFullPlayerRanking = createAction(
    'Reload full player ranking',
    props<{ pageNumber: number, pageSize: number}>()
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

export const changePageLatestGames = createAction(
    'Change page in player ranking',
    props<{ pageNumber: number, pageSize: number }>()
)

export const reloadLatestGames = createAction(
    'Reload full latest games',
    props<{ pageNumber: number, pageSize: number }>()
)

export const previousLatestGamesLoaded = createAction(
    'Previous page of latest games loaded',
    props<Page<GameSnippetView>>()
)

export const currentLatestGamesLoaded = createAction(
    'Current page of latest games loaded',
    props<Page<GameSnippetView>>()
)

export const nextLatestGamesLoaded = createAction(
    'Next page of latest games loaded',
    props<Page<GameSnippetView>>()
)

// Common - TODO
export const commonPageSizeChange = createAction(
    'Common page size change',
    props<{ pageSize: number }>()
)