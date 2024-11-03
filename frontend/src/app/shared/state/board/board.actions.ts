import { createAction, props } from "@ngrx/store";
import { BoardLegalMovesResponse } from "../../model/BoardLegalMovesResponse";
import { BoardView } from "../../model/BoardView";
import { CheckLegalMovesRequest } from "../../model/CheckLegalMovesRequest";
import { IllegalMoveResponse } from "../../model/IllegalMoveView";
import { InitializeBoardRequest } from "../../model/InitializeBoardRequest";
import { InitializedBoardResponse } from "../../model/InitializedBoardResponse";
import { MovePieceRequest } from "../../model/MovePieceRequest";
import { PieceDraggedInfo } from "../../model/PieceDraggedInfo";
import { RefreshBoardDto } from "../../model/RefreshBoardRequest";
import { BoardGameObtainedInfoView } from "../../model/BoardGameObtainedInfoView";
import { GameFinishedView } from "../../model/GameFinishedView";

export const movePiece = createAction(
    'Move piece',
    props<MovePieceRequest>()
)

export const initializeBoard = createAction(
    'Initialize board',
    props<InitializeBoardRequest>()
)

export const boardLoaded = createAction(
    'Board loaded',
    props<BoardView>()
)

export const illegalMove = createAction(
    'Illegal move',
    props<IllegalMoveResponse>()
)

export const draggedPieceChanged = createAction(
    'Dragged piece changed',
    props<PieceDraggedInfo>()
)

export const legalMovesChanged = createAction(
    'Legal moves set changed',
    props<BoardLegalMovesResponse>()
)

export const checkLegalMoves = createAction(
    'Check legal moves by piece on square',
    props<CheckLegalMovesRequest>()
)

export const draggedPieceReleased = createAction(
    'Dragged piece was released',
    props<any>()
)

export const boardInitialized = createAction(
    'User joined, initialized or joined as spectator to game',
    props<InitializedBoardResponse>()
)

export const refreshBoard = createAction(
    'Manual board refresh',
    props<RefreshBoardDto>()
)

export const refreshBoardWithPreMoves = createAction(
    'Manual refresh of board with premoves',
    props<RefreshBoardDto>()
)

export const gameFinished = createAction(
    'Game finished',
    props<BoardGameObtainedInfoView>()
)

export const gameFinishedLoaded = createAction(
    'Game finished view loaded', 
    props<GameFinishedView>()
)
