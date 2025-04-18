import { createReducer, on } from "@ngrx/store";
import { ActiveGamesView } from "../../model/game/ActiveBoardsView";
import { activeBoardsRefreshed, newActiveBoardObtained, removeFinishedBoardFromActiveBoard } from "../../../shared/state/active-boards/active-boards.actions";
import { ActiveGameNewView } from "../../model/game/ActiveBoardNewView";

export interface ActiveBoardsState {
    activeBoardsView: ActiveGamesView
}

export const initialState: ActiveBoardsState = {
    activeBoardsView: {
        activeGameIds: []
    }
}

export const activeBoardsReducer = createReducer(
    initialState,

    // Full list of active boards obtained
    on(
        activeBoardsRefreshed,
        (state:  ActiveBoardsState, content: ActiveGamesView) => (
            {
                ...state,
                activeBoardsView: content
            }
        )
    ),

    // New active board obtained
    on(
        newActiveBoardObtained,
        (state: ActiveBoardsState, content: ActiveGameNewView) => (
            {
                ...state,
                activeBoardsView: {
                    activeGameIds: state.activeBoardsView.activeGameIds.includes(content.gameId) 
                        ? state.activeBoardsView.activeGameIds 
                        :  [...state.activeBoardsView.activeGameIds, content.gameId]
                }
            }
        )
    ),

    // Game on board finished, remove board from active boards
    on(
        removeFinishedBoardFromActiveBoard,
        (state: ActiveBoardsState, content: {gameId: string}) => (
            {
                ...state,
                activeBoardsView: {
                    activeGameIds: state.activeBoardsView.activeGameIds.filter(it => it != content.gameId)
                }
            }
        )
    )
)