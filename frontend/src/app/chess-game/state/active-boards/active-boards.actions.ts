import { createAction, props } from "@ngrx/store";
import { ActiveBoardsView } from "../../model/ActiveBoardsView";
import { ActiveBoardNewView } from "../../model/ActiveBoardNewView";

export const activeBoardsRefreshed = createAction(
    'Refreshed list with active boards',
    props<ActiveBoardsView>()
)

export const refreshActiveBoards = createAction(
    'Refresh list with active boards',
    props<any>()
)

export const newActiveBoardObtained = createAction(
    'New active board obtained',
    props<ActiveBoardNewView>()
)

export const removeFinishedBoardFromActiveBoard = createAction(
    'Game on board was finished, remove board from active boards',
    props<{boardId: string}>()
)