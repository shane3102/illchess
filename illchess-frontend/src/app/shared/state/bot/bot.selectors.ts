import { createSelector } from "@ngrx/store";
import { ChessGameState } from "../chess-game.state";
import { BotState } from "./bot.reducers";

export const botSelector = (state: ChessGameState) => state.botState

export const botListSelector = createSelector(
    botSelector,
    (state: BotState) => state.bots
)

export const botManagmentShown = createSelector(
    botSelector,
    (state: BotState) => state.isShowed
)

export const botMaxCount = createSelector(
    botSelector,
    (state: BotState) => state.maxBotsCount
)