import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { AddBotRequest } from "../model/stockfish/AddBotRequest";
import { BestMoveAndContinuationResponse } from "../model/stockfish/BestMoveAndContinuationResponse";
import { DeleteBotRequest } from "../model/stockfish/DeleteBotRequest";
import { EvaluationResponse } from "../model/stockfish/EvaluationResponse";
import { BotView } from "../model/stockfish/BotView";

@Injectable({
    providedIn: 'root'
})
export class StockfishService {

    readonly PATH: string = `/gateway/stockfish/api`

    private http: HttpClient = inject(HttpClient)

    async evaluateBoard(boardId: string): Promise<EvaluationResponse> {
        return firstValueFrom(
            this.http.get<EvaluationResponse>(`${this.PATH}/board/${boardId}/evaluate`)
        )
    }

    async establishBestMoveAndContinuation(boardId: string): Promise<BestMoveAndContinuationResponse> {
        return firstValueFrom(
            this.http.get<BestMoveAndContinuationResponse>(`${this.PATH}/board/${boardId}/best-move-and-continuation`)
        )
    }

    async addBotsPlayingChess(addBotsRequest: AddBotRequest): Promise<void> {
        return firstValueFrom(
            this.http.post<void>(`${this.PATH}/bot`, addBotsRequest)
        )
    }

    async deleteBotsPlayingChess(deleteBotsRequest: DeleteBotRequest): Promise<void> {
        return firstValueFrom(
            this.http.delete<void>(`${this.PATH}/bot`, { body: deleteBotsRequest })
        )
    }

    async loadCurrenttlyRunBots(): Promise<BotView[]> {
        return firstValueFrom(
            this.http.get<BotView[]>(`${this.PATH}/bot/list`)
        )
    }

    async loadMaxBotCount(): Promise<number> {
        return firstValueFrom(
            this.http.get<number>(`${this.PATH}/bot/max`)
        )
    }

    async loadBotExpirationMinutes(): Promise<number> {
        return firstValueFrom(
            this.http.get<number>(`${this.PATH}/bot/expiration-minutes`)
        )
    }

}
