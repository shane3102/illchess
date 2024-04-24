import { createAction, props } from "@ngrx/store";
import { AcceptDrawRequest } from "../../model/AcceptDrawRequest";
import { BoardAdditionalInfoView } from "../../model/BoardAdditionalInfoView";
import { ProposeDrawRequest } from "../../model/ProposeDrawRequest";
import { RefreshBoardDto } from "../../model/RefreshBoardRequest";
import { RejectDrawRequest } from "../../model/RejectDrawRequest";
import { ResignGameRequest } from "../../model/ResignGameRequest";

export const refreshAdditionalInfoOfBoard = createAction(
    'Manual refresh of board additional info',
    props<RefreshBoardDto>()
)

export const boardAdditionalInfoLoaded = createAction(
    'Board additional info was loaded',
    props<BoardAdditionalInfoView>()
)

export const resignGame = createAction(
    'Resign game',
    props<ResignGameRequest>()
)

export const proposeDraw = createAction(
    'Propose draw',
    props<ProposeDrawRequest>()
)

export const rejectDraw = createAction(
    'Reject draw',
    props<RejectDrawRequest>()
)

export const acceptDraw = createAction(
    'Accept draw',
    props<AcceptDrawRequest>()
)