import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { boardLoaded, checkLegalMoves, boardInitialized, draggedPieceReleased, illegalMove, initializeBoard, legalMovesChanged, movePiece, refreshBoard, refreshBoardWithPreMoves, gameFinished, gameFinishedLoaded, boardLoadingError, quitNotYetStartedGame } from "./board.actions";
import { Observable, catchError, from, map, of, switchMap, tap } from "rxjs";
import { GameService } from "../../service/GameService";
import { BoardLegalMovesResponse } from "../../model/game/BoardLegalMovesResponse";
import { CheckLegalMovesRequest } from "../../model/game/CheckLegalMovesRequest";
import { IllegalMoveView } from "../../model/game/IllegalMoveView";
import { InitializedGameResponse } from "../../model/game/InitializedGameResponse";
import { GameView } from "../../model/game/BoardView";
import { RefreshBoardDto as RefreshBoardRequest } from "../../model/game/RefreshBoardRequest";
import { PlayerInfoService } from "../../service/PlayerInfoService";
import { GameObtainedInfoView } from "../../model/game/BoardGameObtainedInfoView";
import { GameFinishedView } from "../../model/player-info/GameFinishedView";
import { Router } from "@angular/router";
import { QuitOccupiedGameRequest } from "../../model/game/QuitOccupiedGameRequest";

@Injectable({
    providedIn: 'root'
})
export class BoardEffects {

    private actions$ = inject(Actions)
    private chessBoardService = inject(GameService)
    private playerInfoService = inject(PlayerInfoService)
    private router = inject(Router)

    initializeBoard$ = createEffect(
        () => this.actions$.pipe(
            ofType(initializeBoard),
            switchMap(
                (initializeBoardRequest) => from(
                    this.chessBoardService.initializeBoard(initializeBoardRequest)
                )
                    .pipe(
                        map((response: InitializedGameResponse) => boardInitialized(response))
                    )
            )
        )
    )

    movePiece$ = createEffect(
        () => this.actions$.pipe(
            ofType(movePiece),
            switchMap(
                (movePieceRequest) => from(this.chessBoardService.movePiece(movePieceRequest))
                    .pipe(
                        switchMap(() => of(refreshBoardWithPreMoves({ boardId: movePieceRequest.gameId, username: movePieceRequest.username }))),
                        catchError((response: any) => {
                            let body: IllegalMoveView = response.error;
                            return of(illegalMove(body))
                        })
                    )
            )
        )
    )

    checkLegalMoves$ = createEffect(
        () => this.actions$.pipe(
            ofType(checkLegalMoves),
            switchMap(
                (request: CheckLegalMovesRequest) => from(this.chessBoardService.getLegalMoves(request))
                    .pipe(
                        map((response: BoardLegalMovesResponse) => legalMovesChanged(response))
                    )
            )
        )
    )

    connectToBoard$ = createEffect(
        () => this.actions$.pipe(
            ofType(refreshBoard),
            switchMap(
                (dto: RefreshBoardRequest) => from(this.chessBoardService.refreshBoard(dto.boardId))
                    .pipe(
                        map((response: GameView) => boardLoaded(response)),
                        catchError(() => of(boardLoadingError({})))
                    )
            )
        )
    )

    refreshBoardWithPremovesOfUsername$ = createEffect(
        () => this.actions$.pipe(
            ofType(refreshBoardWithPreMoves),
            switchMap(
                (dto: RefreshBoardRequest) => from(this.chessBoardService.refreshBoardWithPremoves(dto.boardId, dto.username!))
                    .pipe(
                        map(
                            ((response: GameView) => boardLoaded(response))
                        )
                    )
            )
        )
    )

    obtainFinishedGameView$ = createEffect(
        () => this.actions$.pipe(
            ofType(gameFinished),
            switchMap(
                (dto: GameObtainedInfoView) => from(this.playerInfoService.getFinishedGameById(dto.gameId))
                .pipe(
                    map(
                        ((response: GameFinishedView) => gameFinishedLoaded(response))
                    )
                )
            )
        )
    )

    redirectOnBoardNotFound$ = createEffect(
        () => this.actions$.pipe(
            ofType(boardLoadingError),
            tap(
                () => this.router.navigateByUrl('')
            )
        ),
        {
            dispatch:  false
        }
    )

    quitNotYetStartedGame$ = createEffect(
        () => this.actions$.pipe(
            ofType(quitNotYetStartedGame),
            tap(
                (dto: QuitOccupiedGameRequest) => {
                    this.chessBoardService.quitNotYetStartedGame(dto)
                    this.router.navigateByUrl('')
                }
            )
        ),
        {
            dispatch: false
        }
    )

}