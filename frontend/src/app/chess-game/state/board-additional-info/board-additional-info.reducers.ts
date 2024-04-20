import { createReducer, on } from "@ngrx/store";
import { BoardAdditionalInfoView } from "../../model/BoardAdditionalInfoView";
import { boardAdditionalInfoLoaded } from "./board-additional-info.actions";

export interface BoardAdditionalInfoState {
    boardAdditionalInfoView: BoardAdditionalInfoView;
}

export const initialState: BoardAdditionalInfoState = {
    boardAdditionalInfoView: {
        boardId:'',
        currentPlayerColor: '',
        gameState: 'CONTINUE',
        capturedWhitePieces: [],
        capturedBlackPieces: [],
        performedMoves: []
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
    )
)