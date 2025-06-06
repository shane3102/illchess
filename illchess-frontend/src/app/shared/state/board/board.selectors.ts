import { createSelector } from "@ngrx/store";
import { RefreshBoardDto } from "../../model/game/RefreshBoardRequest";
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
            "gameId": state.boardView.gameId,
            "highlightSquare": state.illegalMoveHighlightSquare,
            "message": state.illegalMoveMessage
        }
    }
)

export const selectedPieceSelector = createSelector(
    selectBoard,
    (state: BoardState) => {
        return state.pieceSelectedInfo
    }
)

export const legalMovesSelector = createSelector(
    selectBoard,
    (state: BoardState) => {
        return state.legalMoves
    }
)

export const initializedGameIdSelector = createSelector(
    selectBoard,
    (state: BoardState) => {
        return { "gameId": state.boardView.gameId } as RefreshBoardDto
    }
)

export const boardGameObtainedInfoView = createSelector(
    selectBoard,
    (state: BoardState) => {
        return state.gameFinishedInfo.boardGameObtainedInfoView
    }
)

export const gameFinishedView = createSelector(
    selectBoard,
    (state: BoardState) => {
        return state.gameFinishedInfo.gameFinishedView
    }
)
