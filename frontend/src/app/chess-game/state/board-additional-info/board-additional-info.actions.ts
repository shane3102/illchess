import { createAction, props } from "@ngrx/store";
import { RefreshBoardDto } from "../../model/RefreshBoardRequest";
import { BoardAdditionalInfoView } from "../../model/BoardAdditionalInfoView";

export const refreshAdditionalInfoOfBoard = createAction(
    'Manual refresh of board additional info',
    props<RefreshBoardDto>()
)

export const boardAdditionalInfoLoaded = createAction(
    'Board additional info was loaded',
    props<BoardAdditionalInfoView>()
)