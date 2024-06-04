import { createReducer, on } from "@ngrx/store";
import { BoardAdditionalInfoView } from "../../model/BoardAdditionalInfoView";
import { bestMoveAndContinuationLoaded, boardAdditionalInfoLoaded, evaluationLoaded } from "./board-additional-info.actions";
import { EvaluationResponse } from "../../model/EvaluationResponse";
import { BestMoveAndContinuationResponse } from "../../model/BestMoveAndContinuationResponse";
import { state } from "@angular/animations";

export interface BoardAdditionalInfoState {
    boardAdditionalInfoView: BoardAdditionalInfoView,
    evaluation: EvaluationResponse,
    bestMoveAndContinuation: BestMoveAndContinuationResponse
}

export const initialState: BoardAdditionalInfoState = {
    boardAdditionalInfoView: {
        boardId:'',
        currentPlayerColor: '',
        gameState: 'CONTINUE',
        capturedWhitePieces: [],
        capturedBlackPieces: [],
        performedMoves: []
    },
    evaluation: {
        result: 0
    },
    bestMoveAndContinuation: {
        bestMove: "",
        continuation: []
    }
}

export const boardAdditionalInfoReducer = createReducer(
    initialState,

    // update board additional info
    on(
        boardAdditionalInfoLoaded,
        (state: BoardAdditionalInfoState, view: BoardAdditionalInfoView) => (
            {
                ...state,
                boardAdditionalInfoView: view
            }
        )
    ),

    on(
        evaluationLoaded,
        (state: BoardAdditionalInfoState, response: EvaluationResponse) => (
            {
                ...state,
                evaluation: response
            }
        )
    ),

    on(
        bestMoveAndContinuationLoaded,
        (state: BoardAdditionalInfoState, response: BestMoveAndContinuationResponse) => (
            {
                ...state,
                bestMoveAndContinuation: response
            }
        )
    )
)