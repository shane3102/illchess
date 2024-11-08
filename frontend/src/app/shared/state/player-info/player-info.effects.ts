import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { from, map, switchMap, tap, withLatestFrom } from "rxjs";
import { GameSnippetView } from "../../model/player-info/GameSnippetView";
import { LatestGamesLoadDto } from "../../model/player-info/LatestGamesLoadDto";
import { Page } from "../../model/player-info/Page";
import { PlayerView } from "../../model/player-info/PlayerView";
import { PlayerInfoService } from "../../service/PlayerInfoService";
import { commonPageSizeChange, latestGamesLoaded, loadLatestGames, loadPlayerRanking, playerRankingLoaded } from "./player-info.actions";
import { ChessGameState } from "../chess-game.state";
import { Store } from "@ngrx/store";
import { pageNumberPlayerRankingSelector } from "./player-info.selectors";

@Injectable({
    providedIn: 'root'
})
export class PlayerInfoEffects {

    private store = inject(Store<ChessGameState>)
    private actions$ = inject(Actions)
    private playerInfoService = inject(PlayerInfoService)

    loadPlayerRanking$ = createEffect(
        () => this.actions$.pipe(
            ofType(loadPlayerRanking), 
            switchMap(
                (dto: {pageNumberPlayerRanking: number, pageSizePlayerRanking: number }) => from(this.playerInfoService.getPlayerRankingPageable(dto.pageNumberPlayerRanking, dto.pageSizePlayerRanking))
                    .pipe(
                        map((response: Page<PlayerView>) => playerRankingLoaded(response))
                    )
            )
        )
    )

    loadLatestGames$ = createEffect(
        () => this.actions$.pipe(
            ofType(loadLatestGames), 
            switchMap(
                (dto: LatestGamesLoadDto) => from(this.playerInfoService.getLatestGamesPageable(dto.pageNumberLatestGames, dto.pageSizeLatestGames))
                    .pipe(
                        map((response: Page<GameSnippetView>) => latestGamesLoaded(response))
                    )
            )
        )
    )

    loadLatestGamesAndPlayerRankingOnPageSizeChange$ = createEffect(
        () => this.actions$.pipe(
            ofType(commonPageSizeChange),
            tap(
                (dto: { pageSize: number }, ) => {
                    this.store.dispatch(loadLatestGames({ pageNumberLatestGames: 0, pageSizeLatestGames: dto.pageSize }))
                    this.store.dispatch(loadPlayerRanking({ pageNumberPlayerRanking: 0, pageSizePlayerRanking: dto.pageSize }))
                }
            )
        ),
        {
            dispatch: false
        }
    )

}