import { Injectable, inject } from "@angular/core";
import { GameService } from "../../service/GameService";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { activeBoardsRefreshed, refreshActiveBoards } from "./active-boards.actions";
import { from, map, switchMap } from "rxjs";
import { ActiveGamesView } from "../../model/game/ActiveBoardsView";

@Injectable({
    providedIn: 'root'
})
export class ActiveBoardEffects {

    private actions$ = inject(Actions)
    private chessBoardService = inject(GameService) 


    refreshActiveBoards$ = createEffect(
        () => this.actions$.pipe(
            ofType(refreshActiveBoards),
            switchMap(
                () => from(this.chessBoardService.refreshActiveBoards())
                    .pipe(
                        map((response: ActiveGamesView) => activeBoardsRefreshed(response))
                    )
            )
        )
    )
}