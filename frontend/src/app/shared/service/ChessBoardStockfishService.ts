import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { BestMoveAndContinuationResponse } from "../model/BestMoveAndContinuationResponse";
import { EvaluationResponse } from "../model/EvaluationResponse";

@Injectable({
    providedIn: 'root'
})
export class ChessBoardStockfishService {

    readonly PATH: string = `/gateway/stockfish/api/board`

    private http: HttpClient = inject(HttpClient)

    async evaluateBoard(boardId: string): Promise<EvaluationResponse>  {
        return firstValueFrom(
            this.http.get<EvaluationResponse>(`${this.PATH}/evaluate/${boardId}`)
        )
    }

    async establishBestMoveAndContinuation(boardId: string): Promise<BestMoveAndContinuationResponse> {
        return firstValueFrom(
            this.http.get<BestMoveAndContinuationResponse>(`${this.PATH}/best-move-and-continuation/${boardId}`)
        )
    }

}
