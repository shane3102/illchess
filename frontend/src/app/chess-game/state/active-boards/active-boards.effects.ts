import { Injectable } from "@angular/core";
import { ChessBoardService } from "../../service/ChessBoardService";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { activeBoardsRefreshed, refreshActiveBoards } from "./active-boards.actions";
import { from, map, switchMap } from "rxjs";
import { ActiveBoardsView } from "../../model/ActiveBoardsView";

@Injectable({
    providedIn: 'root'
})
export class ActiveBoardEffects {

    constructor(
        private actions$: Actions,
        private chessBoardService: ChessBoardService
    ) {
    }

    refreshActiveBoards$ = createEffect(
        () => this.actions$.pipe(
            ofType(refreshActiveBoards),
            switchMap(
                () => from(this.chessBoardService.refreshActiveBoards())
                    .pipe(
                        map((response: ActiveBoardsView) => activeBoardsRefreshed(response))
                    )
            )
        )
    )
}