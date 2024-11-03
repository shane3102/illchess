import { Injectable } from "@angular/core";
import { ChessBoardService } from "../../../shared/service/ChessBoardService";
import { ComponentStore } from "@ngrx/component-store";
import { BoardView } from "../../../shared/model/BoardView";
import { Observable, from, map, switchMap } from "rxjs";
import { BoardGameObtainedInfoView } from "../../../shared/model/BoardGameObtainedInfoView";

@Injectable()
export class ChessBoardMiniStore extends ComponentStore<ChessBoardMiniState> {

    constructor(private chessBoardService: ChessBoardService) {
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