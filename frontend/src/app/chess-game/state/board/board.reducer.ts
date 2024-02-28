import { createReducer, on } from "@ngrx/store";
import { BoardView } from "../../model/BoardView";
import { PieceColor } from "../../model/PieceInfo";
import { boardLoaded, illegalMove } from "./board.actions";
import { IllegalMoveView } from "../../model/IllegalMoveView";


export interface BoardState {
    boardView: BoardView;
    error: string;
    illegalMoveHighlightSquare: string
    illegalMoveMessage: string
    status: 'pending' | 'loading' | 'error' | 'success'
}

export const initialState: BoardState = {
    boardView: { boardId: '', piecesLocations: {}, currentPlayerColor: PieceColor.WHITE, gameState: 'CONTINUE', victoriousPlayerColor: PieceColor.WHITE },
    error: '',
    illegalMoveHighlightSquare: '',
    illegalMoveMessage: '',
    status: 'pending'
}

export const boardReducer = createReducer(
    initialState,

    // update board
    on(
        boardLoaded,
        (state: BoardState, content: BoardView) => (
            {
                ...state,
                boardView: content,
                illegalMoveHighlightSquare: "",
                illegalMoveMessage: ""
            }
        )
    ),

    // illegal move 
    on(
        illegalMove,
        (state: BoardState, content: IllegalMoveView) => (
            {
                ...state,
                illegalMoveHighlightSquare: content.highlightSquare,
                illegalMoveMessage: content.message
            }
        )
    )

)