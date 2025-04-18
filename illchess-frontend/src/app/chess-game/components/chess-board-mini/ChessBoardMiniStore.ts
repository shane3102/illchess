import { inject } from "@angular/core";
import { patchState, signalStore, withMethods, withState } from "@ngrx/signals";
import { GameObtainedInfoView } from "src/app/shared/model/game/BoardGameObtainedInfoView";
import { GameView } from "src/app/shared/model/game/BoardView";
import { GameService } from "src/app/shared/service/GameService";

interface ChessBoardMiniState {
    boardView: GameView | undefined,
    boardGameObtainedInfoView: GameObtainedInfoView | undefined
}

const initialState: ChessBoardMiniState = {
    boardView: undefined,
    boardGameObtainedInfoView: undefined
}

export const ChessBoardMiniStore = signalStore(
    withState(initialState),
    withMethods(
        (store, gameService = inject(GameService)) => ({
            async refresh(gameId: string): Promise<void> {
                const boardView = await gameService.refreshBoard(gameId)
                patchState(store, {boardView: boardView})
            },
            patchBoardPosition: (boardView: GameView) => {
                patchState(store, { boardView: boardView })
            },
            patchObtainedInfoView: (boardGameObtainedInfoView: GameObtainedInfoView) => {
                patchState(store, { boardGameObtainedInfoView: boardGameObtainedInfoView })
            }
        })
    )
)
