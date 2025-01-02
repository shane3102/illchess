import { createAction, props } from "@ngrx/store";
import { AddBotsRequest } from "../../model/stockfish/AddBotsRequest";
import { BotView } from "../../model/stockfish/BotView";
import { DeleteBotsRequest } from "../../model/stockfish/DeleteBotsRequest";

export const addBots = createAction(
    'Add bots playing chess',
    props<AddBotsRequest>()
)

export const deleteBots = createAction(
    'Delete bots playing chess',
    props<DeleteBotsRequest>()
)

export const loadCurrentllyRunBots = createAction(
    'Load bots playing chess',
    props<any>()
)

export const currentllyRunBotsLoaded = createAction(
    'Currentlly bots playing chess loaded',
    props<{ bots: BotView[] }>()
)