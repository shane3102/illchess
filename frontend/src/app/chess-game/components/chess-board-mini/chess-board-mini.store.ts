import { Injectable } from "@angular/core";
import { ChessBoardService } from "../../service/ChessBoardService";
import { ComponentStore } from "@ngrx/component-store";
import { BoardView } from "../../model/BoardView";
import { Observable, from, map, switchMap, tap } from "rxjs";

@Injectable()
export class ChessBoardMiniStore extends ComponentStore<BoardView> {

    constructor(private chessBoardService: ChessBoardService) {
        super()
    }

    boardView$ = this.select((state)=> state)

    refresh = this.effect(
        (boardId$: Observable<string>) => boardId$
            .pipe(
                switchMap(
                    (boardId) => from(this.chessBoardService.refreshBoard(boardId))
                        .pipe(
                            map((boardView: BoardView) => this.setState(boardView))
                        )
                )
            )
    )

}