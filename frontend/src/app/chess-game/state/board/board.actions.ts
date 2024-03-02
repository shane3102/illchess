import { createAction, props } from "@ngrx/store";
import { MovePieceRequest } from "../../model/MovePieceRequest"
import { BoardView } from "../../model/BoardView";
import { IllegalMoveView } from "../../model/IllegalMoveView";
import { InitializeBoardRequest } from "../../model/InitializeBoardRequest";
import { PieceDraggedInfo } from "../../model/PieceDraggedInfo";

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