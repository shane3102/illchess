import { createReducer, on } from "@ngrx/store";
import { BotView } from "../../model/stockfish/BotView";
import { currentllyRunBotsLoaded, maxBotCountLoaded, showOrHideBotsManagement } from "./bot.actions";
import { state } from "@angular/animations";

export interface BotState {
    isShowed: boolean,
    bots: BotView[],
    maxBotsCount: number | undefined
}

export const initialState: BotState = {
    isShowed: false,
    bots: [],
    maxBotsCount: undefined
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
    ),

    on(
        maxBotCountLoaded,
        (state: BotState, loadedMaxCount: {maxCount: number}) => (
            {
                ...state,
                maxBotsCount: loadedMaxCount.maxCount
            }
        )
    )
)