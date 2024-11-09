import { createReducer, on } from "@ngrx/store";
import { BoardAdditionalInfoView } from "../../model/game/BoardAdditionalInfoView";
import { BestMoveAndContinuationResponse } from "../../model/stockfish/BestMoveAndContinuationResponse";
import { EvaluationResponse } from "../../model/stockfish/EvaluationResponse";
import { boardInitialized } from "../board/board.actions";
import { bestMoveAndContinuationLoaded, boardAdditionalInfoLoaded, establishBestMoveAndContinuation, evaluationLoaded } from "./board-additional-info.actions";

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
    ),

    on(
        establishBestMoveAndContinuation,
        (state: BoardAdditionalInfoState) => (
            {
                ...state,
                bestMoveAndContinuation: {
                    bestMove: "",
                    continuation: []
                }
            }
        )
    ),

    on(
        boardInitialized,
        () => (initialState)
    )
)