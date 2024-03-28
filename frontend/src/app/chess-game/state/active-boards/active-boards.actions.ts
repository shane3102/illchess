import { createAction, props } from "@ngrx/store";
import { ActiveBoardsView } from "../../model/ActiveBoardsView";

export const activeBoardsRefreshed = createAction(
    'Refreshed list with active boards',
    props<ActiveBoardsView>()
)

export const refreshActiveBoards = createAction(
    'Refresh list with active boards',
    props<any>()
)