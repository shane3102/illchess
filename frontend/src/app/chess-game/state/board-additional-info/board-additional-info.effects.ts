import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { from, map, switchMap } from "rxjs";
import { BoardAdditionalInfoView } from "../../model/BoardAdditionalInfoView";
import { RefreshBoardDto } from "../../model/RefreshBoardRequest";
import { ChessBoardService } from "../../service/ChessBoardService";
import { boardAdditionalInfoLoaded, refreshAdditionalInfoOfBoard, resignGame } from "./board-additional-info.actions";
import { ResignGameRequest } from "../../model/ResignGameRequest";

@Injectable({
    providedIn: 'root'
})
export class BoardAdditionalInfoEffects {

    constructor(
        private actions$: Actions,
        private chessBoardService: ChessBoardService
    ) { }

    refreshBoardAdditionalInfo$ = createEffect(
        () => this.actions$.pipe(
            ofType(refreshAdditionalInfoOfBoard),
            switchMap(
                (dto: RefreshBoardDto) => from(this.chessBoardService.refreshBoardAdditionalInfo(dto.boardId))
                    .pipe(
                        map((response: BoardAdditionalInfoView) => boardAdditionalInfoLoaded(response))
                    )
            )
        )
    )

    resignGame$ = createEffect(
        () => this.actions$.pipe(
            ofType(resignGame),
            switchMap(
                (dto: ResignGameRequest) => from(this.chessBoardService.resignGame(dto))
            )
        ),
        {
            dispatch:  false
        }
    )
}