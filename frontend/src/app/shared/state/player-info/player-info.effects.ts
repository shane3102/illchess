import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { Store } from "@ngrx/store";
import { from, map, switchMap, tap } from "rxjs";
import { Page } from "../../model/player-info/Page";
import { PlayerView } from "../../model/player-info/PlayerView";
import { PlayerInfoService } from "../../service/PlayerInfoService";
import { ChessGameState } from "../chess-game.state";
import { playerRankingLoaded, loadPlayerRanking, nextPagePlayerRanking, previousPagePlayerRanking, nextPageLatestGames, loadLatestGames, previousPageLatestGames, latestGamesLoaded} from "./player-info.actions";
import { GameSnippetView } from "../../model/player-info/GameSnippetView";

@Injectable({
    providedIn: 'root'
})
export class PlayerInfoEffects {

    private actions$ = inject(Actions)
    private playerInfoService = inject(PlayerInfoService)
    private store = inject(Store<ChessGameState>)

    loadPlayerRankingOnNextPage$ = createEffect(
        () => this.actions$.pipe(
            ofType(nextPagePlayerRanking),
            tap(
                (dto: { pageNumber: number, pageSize: number}) => {
                    this.store.dispatch(loadPlayerRanking({ pageNumber: dto.pageNumber, pageSize: dto.pageSize }))
                }
            )
        ),
        {
            dispatch: false
        }
    )

    loadPlayerRankingOnPreviousPage = createEffect(
        () => this.actions$.pipe(
            ofType(previousPagePlayerRanking),
            tap(
                (dto: { pageNumber: number, pageSize: number}) => {
                    this.store.dispatch(loadPlayerRanking({ pageNumber: dto.pageNumber, pageSize: dto.pageSize }))
                }
            )
        ),
        {
            dispatch: false
        }
    )

    loadPlayerRanking$ = createEffect(
        () => this.actions$.pipe(
            ofType(loadPlayerRanking), 
            switchMap(
                (dto: {pageNumber: number, pageSize: number}) => from(this.playerInfoService.getPlayerRankingPageable(dto.pageNumber, dto.pageSize))
                    .pipe(
                        map((response: Page<PlayerView>) => playerRankingLoaded(response))
                    )
            )
        )
    )

    loadLatestGamesOnNextPage$ = createEffect(
        () => this.actions$.pipe(
            ofType(nextPageLatestGames),
            tap(
                (dto: { pageNumber: number, pageSize: number}) => {
                    this.store.dispatch(loadLatestGames({ pageNumber: dto.pageNumber, pageSize: dto.pageSize }))
                }
            )
        ),
        {
            dispatch: false
        }
    )

    loadLatestGamesOnPreviousPage = createEffect(
        () => this.actions$.pipe(
            ofType(previousPageLatestGames),
            tap(
                (dto: { pageNumber: number, pageSize: number}) => {
                    this.store.dispatch(loadLatestGames({ pageNumber: dto.pageNumber, pageSize: dto.pageSize }))
                }
            )
        ),
        {
            dispatch: false
        }
    )

    loadLatestGames$ = createEffect(
        () => this.actions$.pipe(
            ofType(loadLatestGames), 
            switchMap(
                (dto: {pageNumber: number, pageSize: number}) => from(this.playerInfoService.getLatestGamesPageable(dto.pageNumber, dto.pageSize))
                    .pipe(
                        map((response: Page<GameSnippetView>) => latestGamesLoaded(response))
                    )
            )
        )
    )

}