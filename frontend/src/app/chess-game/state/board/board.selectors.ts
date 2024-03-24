import { createSelector } from "@ngrx/store";
import { ChessGameState } from "../chess-game.state";
import { BoardState } from "./board.reducer";
import { state } from "@angular/animations";
import { RefreshBoardDto } from "../../model/RefreshBoardRequest";

export const selectBoard = (state: ChessGameState) => state.boardState;

export const boardSelector = createSelector(
    selectBoard,
    (state: BoardState) => state.boardView
);
export const invalidMoveSelector = createSelector(
    selectBoard,
    (state: BoardState) => {
        return {
            "boardId": state.boardView.boardId,
            "highlightSquare": state.illegalMoveHighlightSquare,
            "message": state.illegalMoveMessage
        }
    }
)

export const draggedPieceSelector = createSelector(
    selectBoard,
    (state: BoardState) => {
        return state.pieceDraggedInfo
    }
)

export const legalMovesSelector = createSelector(
    selectBoard,
    (state: BoardState) => {
        return state.legalMoves
    }
)

export const initializedBoardIdSelector = createSelector(
    selectBoard,
    (state: BoardState) => {
        return { "boardId": state.boardView.boardId } as RefreshBoardDto
    }
)
