import { createReducer, on } from "@ngrx/store";
import { BoardLegalMovesResponse } from "../../model/BoardLegalMovesResponse";
import { BoardView } from "../../model/BoardView";
import { InitializedBoardResponse } from "../../model/InitializedBoardResponse";
import { PieceDraggedInfo } from "../../model/PieceDraggedInfo";
import { boardInitialized, boardLoaded, draggedPieceChanged, draggedPieceReleased, gameFinished, gameFinishedLoaded, illegalMove, initializeBoard, legalMovesChanged, movePiece } from "./board.actions";
import { BoardGameObtainedInfoView } from "../../model/BoardGameObtainedInfoView";
import { GameFinishedView } from "../../model/GameFinishedView";


export interface BoardState {
    boardView: BoardView;
    error: string;
    illegalMoveHighlightSquare: string
    illegalMoveMessage: string
    pieceDraggedInfo?: PieceDraggedInfo,
    legalMoves?: BoardLegalMovesResponse,
    gameFinishedInfo: {boardGameObtainedInfoView?: BoardGameObtainedInfoView, gameFinishedView?: GameFinishedView}
    status: 'pending' | 'loading' | 'error' | 'success'
}

export const initialState: BoardState = {
    boardView: { boardId: '', piecesLocations: {}},
    error: '',
    illegalMoveHighlightSquare: '',
    illegalMoveMessage: '',
    legalMoves: undefined,
    gameFinishedInfo: {},
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
                gameFinishedInfo: {},
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
                gameFinishedInfo: {},
                boardView: {
                    ...state.boardView,
                    "boardId": content.id
                }
            }
        )
    ),

    // initializing new board - remove all data of previous board
    on(
        initializeBoard,
        (state: BoardState) => (
            {
                ...state,
                boardView: { boardId: '', piecesLocations: {}}
            }
        )
    ),

    // game finished info obtained
    on(
        gameFinished,
        (state: BoardState, content: BoardGameObtainedInfoView) => (
            {
                ...state,
                gameFinishedInfo: {...state.gameFinishedInfo, boardGameObtainedInfoView: content}
            }
        ) 
    ),

    // game finished view obtained
    on(
        gameFinishedLoaded,
        (state: BoardState, content: GameFinishedView) =>(
            {
                ...state,
                gameFinishedInfo: {...state.gameFinishedInfo, gameFinishedView: content}
            }
        )
    )

)
