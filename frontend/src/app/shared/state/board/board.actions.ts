import { createAction, props } from "@ngrx/store";
import { BoardLegalMovesResponse } from "../../model/game/BoardLegalMovesResponse";
import { BoardView } from "../../model/game/BoardView";
import { CheckLegalMovesRequest } from "../../model/game/CheckLegalMovesRequest";
import { IllegalMoveResponse } from "../../model/game/IllegalMoveView";
import { InitializeBoardRequest } from "../../model/game/InitializeBoardRequest";
import { InitializedBoardResponse } from "../../model/game/InitializedBoardResponse";
import { MovePieceRequest } from "../../model/game/MovePieceRequest";
import { PieceDraggedInfo } from "../../model/game/PieceDraggedInfo";
import { RefreshBoardDto } from "../../model/game/RefreshBoardRequest";
import { BoardGameObtainedInfoView } from "../../model/game/BoardGameObtainedInfoView";
import { GameFinishedView } from "../../model/player-info/GameFinishedView";

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

export const boardLoadingError = createAction(
    'Board loading error',
    props<any>()
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

