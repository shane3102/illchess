import { createAction, props } from "@ngrx/store";
import { AcceptDrawRequest } from "../../model/game/AcceptDrawRequest";
import { BoardAdditionalInfoView } from "../../model/game/BoardAdditionalInfoView";
import { ProposeDrawRequest } from "../../model/game/ProposeDrawRequest";
import { RefreshBoardDto } from "../../model/game/RefreshBoardRequest";
import { RejectDrawRequest } from "../../model/game/RejectDrawRequest";
import { ResignGameRequest } from "../../model/game/ResignGameRequest";
import { EvaluationResponse } from "../../model/stockfish/EvaluationResponse";
import { BestMoveAndContinuationResponse } from "../../model/stockfish/BestMoveAndContinuationResponse";
import { ProposeTakingBackMoveRequest } from "../../model/game/ProposeTakingBackMoveRequest";
import { RejectTakingBackMoveRequest } from "../../model/game/RejectTakingBackMoveRequest";
import { AcceptTakingBackMoveRequest } from "../../model/game/AcceptTakingBackMoveRequest";

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

export const proposeTakingBackMove = createAction(
    'Propose taking back move',
    props<ProposeTakingBackMoveRequest>()
)

export const rejectTakingBackMove = createAction(
    'Reject taking back move',
    props<RejectTakingBackMoveRequest>()
)

export const acceptTakingBackMove = createAction(
    'Accept taking back move',
    props<AcceptTakingBackMoveRequest>()
)

export const establishEvaluation = createAction(
    'Establish board evaluation of engine',
    props<{boardId: string}>()
)

export const evaluationLoaded = createAction(
    'Evaluation of board loaded',
    props<EvaluationResponse>()
)

export const establishBestMoveAndContinuation = createAction(
    'Establish best move and following continuation',
    props<{boardId: string}>()
)

export const bestMoveAndContinuationLoaded = createAction(
    'Best move and continuation of board loaded',
    props<BestMoveAndContinuationResponse>()
)