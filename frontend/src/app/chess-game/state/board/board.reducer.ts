import { createReducer, on } from "@ngrx/store";
import { BoardView, Square } from "../../model/BoardView";
import { PieceColor } from "../../model/PieceInfo";
import { boardLoaded, draggedPieceChanged, illegalMove, legalMovesChanged, movePiece } from "./board.actions";
import { IllegalMoveView } from "../../model/IllegalMoveView";
import { PieceDraggedInfo } from "../../model/PieceDraggedInfo";
import { state } from "@angular/animations";
import { BoardLegalMovesResponse } from "../../model/BoardLegalMovesResponse";
import { MovePieceRequest } from "../../model/MovePieceRequest";


export interface BoardState {
    boardView: BoardView;
    error: string;
    illegalMoveHighlightSquare: string
    illegalMoveMessage: string
    pieceDraggedInfo?: PieceDraggedInfo,
    legalMoves?: BoardLegalMovesResponse
    status: 'pending' | 'loading' | 'error' | 'success'
}

export const initialState: BoardState = {
    boardView: { boardId: '', piecesLocations: {}, currentPlayerColor: PieceColor.WHITE, gameState: 'CONTINUE'},
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
    ),

    // dragged piece changed
    on(
        draggedPieceChanged,
        (state: BoardState, content: PieceDraggedInfo) => (
            {
                ...state,
                pieceDraggedInfo: content,
                illegalMoveHighlightSquare: "",
                illegalMoveMessage: ""
            }
        )
    ),

    // legal moves changed
    on(
        legalMovesChanged,
        (state: BoardState, content: BoardLegalMovesResponse) => (
            {
                ...state,
                legalMoves: content
            }
        )
    ),

    // reset legal moves if move performed
    on(
        movePiece,
        (state: BoardState)  => (
            {
                ...state,
                legalMoves: undefined
            }
        )
    )

)