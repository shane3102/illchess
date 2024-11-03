import { ActiveBoardsState } from "src/app/shared/state/active-boards/active-boards.reducer";
import { BoardAdditionalInfoState } from "./board-additional-info/board-additional-info.reducers";
import { BoardState } from "./board/board.reducer";

export interface ChessGameState {
    boardState: BoardState,
    activeBoardsState: ActiveBoardsState
    boardAdditionalInfoState: BoardAdditionalInfoState
}