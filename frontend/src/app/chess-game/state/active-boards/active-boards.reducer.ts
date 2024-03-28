import { createReducer, on } from "@ngrx/store";
import { ActiveBoardsView } from "../../model/ActiveBoardsView";
import { activeBoardsRefreshed } from "./active-boards.actions";

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
    on(
        activeBoardsRefreshed,
        (state:  ActiveBoardsState, content: ActiveBoardsView) => (
            {
                ...state,
                activeBoardsView: content
            }
        )
    )
)