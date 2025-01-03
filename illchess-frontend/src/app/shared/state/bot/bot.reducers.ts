import { createReducer, on } from "@ngrx/store";
import { BotView } from "../../model/stockfish/BotView";
import { currentllyRunBotsLoaded, showOrHideBotsManagement } from "./bot.actions";

export interface BotState {
    isShowed: boolean,
    bots: BotView[]
}

export const initialState: BotState = {
    isShowed: false,
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
    ),

    on(
        showOrHideBotsManagement,
        (state: BotState) =>(
            {
                ...state,
                isShowed: !state.isShowed
            }
        )
    )
)