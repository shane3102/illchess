import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { checkLegalMoves, initializeBoard, legalMovesChanged, movePiece } from "./board.actions";
import { catchError, from, map, switchMap } from "rxjs";
import { ChessBoardService } from "../../service/ChessBoardService";
import { BoardLegalMovesResponse } from "../../model/BoardLegalMovesResponse";
import { CheckLegalMovesRequest } from "../../model/CheckLegalMovesRequest";

@Injectable({
    providedIn: 'root'
})
export class BoardEffects {

    constructor(
        private actions$: Actions,
        private chessBoardService: ChessBoardService
    ) { }

    initializeBoard$ = createEffect(
        () => this.actions$.pipe(
            ofType(initializeBoard),
            switchMap(
                (initializeBoardRequest) => from(
                    this.chessBoardService.initializeBoard(initializeBoardRequest)
                )
            )
        ),
        { dispatch: false }
    )

    movePiece$ = createEffect(
        () => this.actions$.pipe(
            ofType(movePiece),
            switchMap(
                (movePieceRequest) => from(
                    this.chessBoardService.movePiece(movePieceRequest)
                )
            )
        ),
        { dispatch: false }
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

}