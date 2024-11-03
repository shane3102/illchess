import { Injectable } from "@angular/core";
import { GameService } from "../../service/GameService";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { activeBoardsRefreshed, refreshActiveBoards } from "./active-boards.actions";
import { from, map, switchMap } from "rxjs";
import { ActiveBoardsView } from "../../model/game/ActiveBoardsView";

@Injectable({
    providedIn: 'root'
})
export class ActiveBoardEffects {

    constructor(
        private actions$: Actions,
        private chessBoardService: GameService
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