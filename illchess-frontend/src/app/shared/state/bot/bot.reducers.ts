import { createReducer, on } from "@ngrx/store";
import { BotView } from "../../model/stockfish/BotView";
import { currentllyRunBotsLoaded } from "./bot.actions";

export interface BotState {
    bots: BotView[]
}

export const initialState: BotState = {
    bots: []
}

export const botReducer = createReducer(
    initialState,

    on(
        currentllyRunBotsLoaded,
        (state: BotState, dto: { bots: BotView[] }) => (
            {
                ...state,
                bots: dto.bots
            }
        )
    )
)