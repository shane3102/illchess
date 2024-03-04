import { createAction, props } from "@ngrx/store";
import { MovePieceRequest } from "../../model/MovePieceRequest"
import { BoardView, Square } from "../../model/BoardView";
import { IllegalMoveView } from "../../model/IllegalMoveView";
import { InitializeBoardRequest } from "../../model/InitializeBoardRequest";
import { PieceDraggedInfo } from "../../model/PieceDraggedInfo";
import { BoardLegalMovesResponse } from "../../model/BoardLegalMovesResponse";
import { CheckLegalMovesRequest } from "../../model/CheckLegalMovesRequest";

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
    props<IllegalMoveView>()
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