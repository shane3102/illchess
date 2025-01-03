import { createAction, props } from "@ngrx/store";
import { AddBotRequest } from "../../model/stockfish/AddBotRequest";
import { BotView } from "../../model/stockfish/BotView";
import { DeleteBotRequest } from "../../model/stockfish/DeleteBotRequest";

export const addBots = createAction(
    'Add bots playing chess',
    props<AddBotRequest>()
)

export const deleteBots = createAction(
    'Delete bots playing chess',
    props<DeleteBotRequest>()
)

export const loadCurrentllyRunBots = createAction(
    'Load bots playing chess',
    props<any>()
)

export const currentllyRunBotsLoaded = createAction(
    'Currentlly bots playing chess loaded',
    props<{ bots: BotView[] }>()
)