import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { initializeBoard, movePiece } from "./board.actions";
import { from, switchMap } from "rxjs";
import { ChessBoardService } from "../../service/ChessBoardService";

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



}