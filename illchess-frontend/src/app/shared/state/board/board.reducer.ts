import { createReducer, on } from "@ngrx/store";
import { BoardLegalMovesResponse } from "../../model/game/BoardLegalMovesResponse";
import { GameView } from "../../model/game/BoardView";
import { InitializedGameResponse } from "../../model/game/InitializedGameResponse";
import { PieceSelectedInfo } from "../../model/game/PieceSelectedInfo";
import { boardInitialized, boardLoaded, selectedPieceChanged, draggedPieceReleased, gameFinished, gameFinishedLoaded, illegalMove, initializeBoard, legalMovesChanged, movePiece } from "./board.actions";
import { GameObtainedInfoView } from "../../model/game/BoardGameObtainedInfoView";
import { GameFinishedView } from "../../model/player-info/GameFinishedView";


export interface BoardState {
    boardView: GameView;
    error: string;
    illegalMoveHighlightSquare: string
    illegalMoveMessage: string
    pieceSelectedInfo?: PieceSelectedInfo,
    legalMoves?: BoardLegalMovesResponse,
    gameFinishedInfo: {boardGameObtainedInfoView?: GameObtainedInfoView, gameFinishedView?: GameFinishedView}
    status: 'pending' | 'loading' | 'error' | 'success'
}

export const initialState: BoardState = {
    boardView: { gameId: '', piecesLocations: {}},
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
        (state: BoardState, content: GameView) => (
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

    // selected piece changed
    on(
        selectedPieceChanged,
        (state: BoardState, content: PieceSelectedInfo) => (
            {
                ...state,
                pieceSelectedInfo: content,
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
                pieceSelectedInfo: undefined,
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
        (state: BoardState, content: InitializedGameResponse) => (
            {
                ...state,
                gameFinishedInfo: {},
                boardView: {
                    ...state.boardView,
                    "gameId": content.id
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
                boardView: { gameId: '', piecesLocations: {}}
            }
        )
    ),

    // game finished info obtained
    on(
        gameFinished,
        (state: BoardState, content: GameObtainedInfoView) => (
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
