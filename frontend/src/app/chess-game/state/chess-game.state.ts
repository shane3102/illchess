import { ActiveBoardsState } from "./active-boards/active-boards.reducer";
import { BoardState } from "./board/board.reducer";

export interface ChessGameState {
    boardState: BoardState,
    activeBoardsState: ActiveBoardsState
}