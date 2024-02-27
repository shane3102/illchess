import { createSelector } from "@ngrx/store";
import { ChessGameState } from "../chess-game.state";
import { BoardState } from "./board.reducer";

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
