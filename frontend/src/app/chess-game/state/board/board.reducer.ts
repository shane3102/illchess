import { createReducer, on } from "@ngrx/store";
import { BoardLegalMovesResponse } from "../../model/BoardLegalMovesResponse";
import { BoardView } from "../../model/BoardView";
import { InitializedBoardResponse } from "../../model/InitializedBoardResponse";
import { PieceDraggedInfo } from "../../model/PieceDraggedInfo";
import { PieceColor } from "../../model/PieceInfo";
import { boardInitialized, boardLoaded, draggedPieceChanged, draggedPieceReleased, illegalMove, legalMovesChanged, movePiece } from "./board.actions";


export interface BoardState {
    boardView: BoardView;
    error: string;
    illegalMoveHighlightSquare: string
    illegalMoveMessage: string
    pieceDraggedInfo?: PieceDraggedInfo,
    legalMoves?: BoardLegalMovesResponse,
    status: 'pending' | 'loading' | 'error' | 'success'
}

export const initialState: BoardState = {
    boardView: { boardId: '', piecesLocations: {}, currentPlayerColor: PieceColor.WHITE, gameState: 'CONTINUE', whitePlayer: "", blackPlayer: "" },
    error: '',
    illegalMoveHighlightSquare: '',
    illegalMoveMessage: '',
    legalMoves: undefined,
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
                illegalMoveMessage: "",
                legalMoves: undefined
            }
        )
    ),

    // illegal move
    on(
        illegalMove,
        (state: BoardState, content: any) => (
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
        (state: BoardState) => (
            {
                ...state,
                legalMoves: undefined
            }
        )
    ),

    // dragged piece released
    on(
        draggedPieceReleased,
        (state: BoardState) => (
            {
                ...state,
                legalMoves: undefined
            }
        )
    ),

    // board was initialized
    on(
        boardInitialized,
        (state: BoardState, content: InitializedBoardResponse) => (
            {
                ...state,
                boardView: {
                    ...state.boardView,
                    "boardId": content.id
                }
            }
        )
    )

)
