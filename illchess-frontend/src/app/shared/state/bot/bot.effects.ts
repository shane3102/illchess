import { inject, Injectable } from "@angular/core";
import { StockfishService } from "../../service/StockfishService";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { addBots, currentllyRunBotsLoaded, deleteBots, loadCurrentllyRunBots, showOrHideBotsManagement } from "./bot.actions";
import { from, map, switchMap } from "rxjs";
import { BotView } from "../../model/stockfish/BotView";
import { AddBotRequest } from "../../model/stockfish/AddBotRequest";
import { DeleteBotRequest } from "../../model/stockfish/DeleteBotRequest";

@Injectable({
    providedIn: 'root'
})
export class BotEffects {

    private actions$ = inject(Actions)
    private stockfishService = inject(StockfishService)

    loadCurrentllyRunBots$ = createEffect(
        () => this.actions$.pipe(
            ofType(loadCurrentllyRunBots, showOrHideBotsManagement), 
            switchMap(
                () => from(this.stockfishService.loadCurrenttlyRunBots())
                    .pipe(
                        map((bots: BotView[]) => currentllyRunBotsLoaded({bots: bots}))
                    )
            )
        )
    )

    addBots$ = createEffect(
        () => this.actions$.pipe(
            ofType(addBots),
            switchMap(
                (request: AddBotRequest) => from(this.stockfishService.addBotsPlayingChess(request))
                    .pipe(
                        map(() => loadCurrentllyRunBots({}))
                    )
            )
        )
    )

    deleteBots$ = createEffect(
        () => this.actions$.pipe(
            ofType(deleteBots),
            switchMap(
                (request: DeleteBotRequest) => from(this.stockfishService.deleteBotsPlayingChess(request))
                    .pipe(
                        map(() => loadCurrentllyRunBots({}))
                    )
            )
        )
    )


}