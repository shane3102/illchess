import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { from, map, switchMap, tap } from "rxjs";
import { PlayerInfoService } from "../../service/PlayerInfoService";
import { currentPagePlayerRankingLoaded, loadCurrentPagePlayerRanking, loadNextPagePlayerRanking, loadPreviousPagePlayerRanking, nextPagePlayerRanking, nextPagePlayerRankingLoaded, previousPagePlayerRanking, previousPagePlayerRankingLoaded, reloadFullPlayerRanking } from "./player-info.actions";
import { PlayerView } from "../../model/player-info/PlayerView";
import { Page } from "../../model/player-info/Page";
import { ChessGameState } from "../chess-game.state";
import { Store } from "@ngrx/store";

@Injectable({
    providedIn: 'root'
})
export class PlayerInfoEffects {

    private actions$ = inject(Actions)
    private playerInfoService = inject(PlayerInfoService)
    private store = inject(Store<ChessGameState>)

    refreshNewPageIfNewPageSelectedAndNotLastPage$ = createEffect(
        () => this.actions$.pipe(
            ofType(nextPagePlayerRanking),
            tap(
                (dto: { pageNumber: number, pageSize: number, totalPages: number }) => {
                    if (dto.pageNumber < dto.totalPages) {
                        this.store.dispatch(loadNextPagePlayerRanking({ nextPageNumber: dto.pageNumber + 1, pageSize: dto.pageSize }))
                    }
                }
            )
        ),
        {
            dispatch: false
        }
    )

    refreshPreviousPageIfNewPageSelectedAndNotFirstPage$ = createEffect(
        () => this.actions$.pipe(
            ofType(previousPagePlayerRanking),
            tap(
                (dto: { pageNumber: number, pageSize: number, totalPages: number }) => {
                    if (dto.pageNumber > 0) {
                        this.store.dispatch(loadPreviousPagePlayerRanking({ previousPageNumber: dto.pageNumber - 1, pageSize: dto.pageSize }))
                    }
                }
            )
        ),
        {
            dispatch: false
        }
    )

    reloadFullPlayerRanking$ = createEffect(
        () => this.actions$.pipe(
            ofType(reloadFullPlayerRanking),
            tap(
                (dto: { pageNumber: number, pageSize: number }) => {
                    this.store.dispatch(loadCurrentPagePlayerRanking({ currentPageNumber: dto.pageNumber, pageSize: dto.pageSize }))
                    this.store.dispatch(loadNextPagePlayerRanking({ nextPageNumber: dto.pageNumber + 1, pageSize: dto.pageSize }))
                    if (dto.pageNumber > 0) {
                        this.store.dispatch(loadPreviousPagePlayerRanking({ previousPageNumber: dto.pageNumber - 1, pageSize: dto.pageSize }))
                    }
                }
            )
        ),
        {
            dispatch: false
        }
    )

    reloadPlayerRankingCurrentPage$ = createEffect(
        () => this.actions$.pipe(
            ofType(loadCurrentPagePlayerRanking), 
            switchMap(
                (dto: {currentPageNumber: number, pageSize: number}) => from(this.playerInfoService.getPlayerRankingPageable(dto.currentPageNumber, dto.pageSize))
                    .pipe(
                        map((response: Page<PlayerView>) => currentPagePlayerRankingLoaded(response))
                    )
            )
        )
    )

    reloadPlayerRankingNextPage$ = createEffect(
        () => this.actions$.pipe(
            ofType(loadNextPagePlayerRanking), 
            switchMap(
                (dto: {nextPageNumber: number, pageSize: number}) => from(this.playerInfoService.getPlayerRankingPageable(dto.nextPageNumber, dto.pageSize))
                    .pipe(
                        map((response: Page<PlayerView>) => nextPagePlayerRankingLoaded(response))
                    )
            )
        )
    )

    reloadPlayerRankingPreviousPage$ = createEffect(
        () => this.actions$.pipe(
            ofType(loadPreviousPagePlayerRanking), 
            switchMap(
                (dto: {previousPageNumber: number, pageSize: number}) => from(this.playerInfoService.getPlayerRankingPageable(dto.previousPageNumber, dto.pageSize))
                    .pipe(
                        map((response: Page<PlayerView>) => previousPagePlayerRankingLoaded(response))
                    )
            )
        )
    )

}