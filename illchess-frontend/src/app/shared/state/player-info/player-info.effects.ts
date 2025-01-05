import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { Store } from "@ngrx/store";
import { from, map, switchMap, tap } from "rxjs";
import { GameSnippetView } from "../../model/player-info/GameSnippetView";
import { LatestGamesLoadDto } from "../../model/player-info/LatestGamesLoadDto";
import { Page } from "../../model/player-info/Page";
import { PlayerView } from "../../model/player-info/PlayerView";
import { PlayerInfoService } from "../../service/PlayerInfoService";
import { ChessGameState } from "../chess-game.state";
import { changeUsername, commonPageSizeChange, generateRandomUsername, latestGamesLoaded, loadLatestGames, loadPlayerRanking, playerRankingLoaded } from "./player-info.actions";
import { GenerateRandomNameService } from "../../service/GenerateRandomNameService";

@Injectable({
    providedIn: 'root'
})
export class PlayerInfoEffects {

    private store = inject(Store<ChessGameState>)
    private actions$ = inject(Actions)
    private playerInfoService = inject(PlayerInfoService)
    private generateRandomNameService = inject(GenerateRandomNameService)

    loadPlayerRanking$ = createEffect(
        () => this.actions$.pipe(
            ofType(loadPlayerRanking),
            switchMap(
                (dto: { pageNumberPlayerRanking: number, pageSizePlayerRanking: number }) => from(this.playerInfoService.getPlayerRankingPageable(dto.pageNumberPlayerRanking, dto.pageSizePlayerRanking))
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
                (dto: { pageSize: number },) => {
                    this.store.dispatch(loadLatestGames({ pageNumberLatestGames: 0, pageSizeLatestGames: dto.pageSize }))
                    this.store.dispatch(loadPlayerRanking({ pageNumberPlayerRanking: 0, pageSizePlayerRanking: dto.pageSize }))
                }
            )
        ),
        {
            dispatch: false
        }
    )

    changeUsername$ = createEffect(
        () => this.actions$.pipe(
            ofType(changeUsername),
            tap(
                (dto: { username: string }) => {
                    sessionStorage.setItem('username', dto.username)
                }
            )
        ),
        {
            dispatch: false
        }
    )

    generateRandomUsername$ = createEffect(
        () => this.actions$.pipe(
            ofType(generateRandomUsername),
            tap(
                () => {
                    let username: string = this.generateRandomNameService.generateRandomName()
                    this.store.dispatch(changeUsername({username: username}))
                }
            )
        ),
        {
            dispatch: false
        }
    )

}