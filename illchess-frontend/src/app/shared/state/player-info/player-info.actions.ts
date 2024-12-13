import { createAction, props } from "@ngrx/store";
import { GameSnippetView } from "../../model/player-info/GameSnippetView";
import { Page } from "../../model/player-info/Page";
import { PlayerView } from "../../model/player-info/PlayerView";
import { PlayerRankingLoadDto } from "../../model/player-info/PlayerRankingLoadDto";
import { LatestGamesLoadDto } from "../../model/player-info/LatestGamesLoadDto";

// Player ranking
export const loadPlayerRanking = createAction(
    'Load page of player ranking',
    props<PlayerRankingLoadDto>()
)

export const playerRankingLoaded = createAction(
    'Current page of player ranking loaded',
    props<Page<PlayerView>>()
)

// Latest games
export const loadLatestGames = createAction(
    'Reload full latest games',
    props<LatestGamesLoadDto>()
)

export const latestGamesLoaded = createAction(
    'Latest games loaded',
    props<Page<GameSnippetView>>()
)

// Common
export const commonPageSizeChange = createAction(
    'Common page size change',
    props<{ pageSize: number }>()
)

// User info
export const changeUsername = createAction(
    'Changing username',
    props<{username: string}>()
)

export const generateRandomUsername = createAction(
    'Generating randoim username', 
    props<any>()
)