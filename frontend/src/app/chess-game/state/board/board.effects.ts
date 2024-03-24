import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { boardLoaded, checkLegalMoves, boardInitialized, draggedPieceReleased, illegalMove, initializeBoard, legalMovesChanged, movePiece, refreshBoard } from "./board.actions";
import { catchError, from, map, of, switchMap } from "rxjs";
import { ChessBoardService } from "../../service/ChessBoardService";
import { BoardLegalMovesResponse } from "../../model/BoardLegalMovesResponse";
import { CheckLegalMovesRequest } from "../../model/CheckLegalMovesRequest";
import { IllegalMoveResponse } from "../../model/IllegalMoveView";
import { InitializedBoardResponse } from "../../model/InitializedBoardResponse";
import { BoardView } from "../../model/BoardView";
import { RefreshBoardDto as RefreshBoardRequest } from "../../model/RefreshBoardRequest";

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
                    .pipe(
                        map((response: InitializedBoardResponse) => boardInitialized(response))
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
                        map(() => draggedPieceReleased({})),
                        catchError((response: any) => {
                            let body: IllegalMoveResponse = response.error;
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
                        map((response: BoardView) => boardLoaded(response))
                    )
            )
        )
    )

}