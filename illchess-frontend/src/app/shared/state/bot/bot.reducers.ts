import { createReducer, on } from "@ngrx/store";
import { BotView } from "../../model/stockfish/BotView";
import { botExpirationMinutesLoaded, currentllyRunBotsLoaded, maxBotCountLoaded, showOrHideBotsManagement } from "./bot.actions";

export interface BotState {
    isShowed: boolean,
    bots: BotView[],
    maxBotsCount: number | undefined,
    botExpirationMinutes: number | undefined
}

export const initialState: BotState = {
    isShowed: false,
    bots: [],
    maxBotsCount: undefined,
    botExpirationMinutes: undefined
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
    ),

    on(
        botExpirationMinutesLoaded,
        (state: BotState, loadedBotExpirationMinutes: {expirationMinutes: number}) => (
            {
                ...state,
                botExpirationMinutes: loadedBotExpirationMinutes.expirationMinutes
            }
        )
    )
)