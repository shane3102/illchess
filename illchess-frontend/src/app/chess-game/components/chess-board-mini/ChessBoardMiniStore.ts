import { inject } from "@angular/core";
import { patchState, signalStore, withMethods, withState } from "@ngrx/signals";
import { BoardGameObtainedInfoView } from "src/app/shared/model/game/BoardGameObtainedInfoView";
import { BoardView } from "src/app/shared/model/game/BoardView";
import { GameService } from "src/app/shared/service/GameService";

interface ChessBoardMiniState {
    boardView: BoardView | undefined,
    boardGameObtainedInfoView: BoardGameObtainedInfoView | undefined
}

const initialState: ChessBoardMiniState = {
    boardView: undefined,
    boardGameObtainedInfoView: undefined
}

export const ChessBoardMiniStore = signalStore(
    withState(initialState),
    withMethods(
        (store, gameService = inject(GameService)) => ({
            async refresh(boardId: string): Promise<void> {
                const boardView = await gameService.refreshBoard(boardId)
                patchState(store, {boardView: boardView})
            },
            patchBoardPosition: (boardView: BoardView) => {
                patchState(store, { boardView: boardView })
            },
            patchObtainedInfoView: (boardGameObtainedInfoView: BoardGameObtainedInfoView) => {
                patchState(store, { boardGameObtainedInfoView: boardGameObtainedInfoView })
            }
        })
    )
)
