import { Injectable, inject } from "@angular/core";
import { GameService } from "../../../shared/service/GameService";
import { ComponentStore } from "@ngrx/component-store";
import { BoardView } from "../../../shared/model/game/BoardView";
import { Observable, from, map, switchMap } from "rxjs";
import { BoardGameObtainedInfoView } from "../../../shared/model/game/BoardGameObtainedInfoView";

@Injectable()
export class ChessBoardMiniStore extends ComponentStore<ChessBoardMiniState> {

    private chessBoardService = inject(GameService)

    constructor() {
        super({})
    }

    boardView$ = this.select((state)=> state.boardView)
    boardGameObtainedInfoView$ = this.select((state)=> state.boardGameObtainedInfoView)

    refresh = this.effect(
        (boardId$: Observable<string>) => boardId$
            .pipe(
                switchMap(
                    (boardId) => from(this.chessBoardService.refreshBoard(boardId))
                        .pipe(
                            map((boardView: BoardView) => this.patchState({boardView: boardView}))
                        )
                )
            )
    )
}

interface ChessBoardMiniState {
    boardView?: BoardView, 
    boardGameObtainedInfoView?: BoardGameObtainedInfoView
}