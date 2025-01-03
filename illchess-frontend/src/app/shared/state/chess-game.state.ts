import { ActiveBoardsState } from "src/app/shared/state/active-boards/active-boards.reducer";
import { BoardAdditionalInfoState } from "./board-additional-info/board-additional-info.reducers";
import { BoardState } from "./board/board.reducer";
import { PlayerInfoState } from "./player-info/player-info.reducers";
import { BotState } from "./bot/bot.reducers";

export interface ChessGameState {
    boardState: BoardState,
    activeBoardsState: ActiveBoardsState
    boardAdditionalInfoState: BoardAdditionalInfoState,
    playerInfoState: PlayerInfoState,
    botState: BotState
}