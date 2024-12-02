import { createReducer, on } from "@ngrx/store";
import { ActiveBoardsView } from "../../model/game/ActiveBoardsView";
import { activeBoardsRefreshed, newActiveBoardObtained, removeFinishedBoardFromActiveBoard } from "../../../shared/state/active-boards/active-boards.actions";
import { ActiveBoardNewView } from "../../model/game/ActiveBoardNewView";

export interface ActiveBoardsState {
    activeBoardsView: ActiveBoardsView
}

export const initialState: ActiveBoardsState = {
    activeBoardsView: {
        activeBoardsIds: []
    }
}

export const activeBoardsReducer = createReducer(
    initialState,

    // Full list of active boards obtained
    on(
        activeBoardsRefreshed,
        (state:  ActiveBoardsState, content: ActiveBoardsView) => (
            {
                ...state,
                activeBoardsView: content
            }
        )
    ),

    // New active board obtained
    on(
        newActiveBoardObtained,
        (state: ActiveBoardsState, content: ActiveBoardNewView) => (
            {
                ...state,
                activeBoardsView: {
                    activeBoardsIds: [...state.activeBoardsView.activeBoardsIds, content.boardId]
                }
            }
        )
    ),

    // Game on board finished, remove board from active boards
    on(
        removeFinishedBoardFromActiveBoard,
        (state: ActiveBoardsState, content: {boardId: string}) => (
            {
                ...state,
                activeBoardsView: {
                    activeBoardsIds: state.activeBoardsView.activeBoardsIds.filter(it => it != content.boardId)
                }
            }
        )
    )
)