import { createSelector } from "@ngrx/store";
import { ChessGameState } from "../chess-game.state";
import { BotState } from "./bot.reducers";

export const botSelector = (state: ChessGameState) => state.botState

export const botListSelector = createSelector(
    botSelector,
    (state: BotState) => state.bots
)