import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { BestMoveAndContinuationResponse } from "../model/stockfish/BestMoveAndContinuationResponse";
import { EvaluationResponse } from "../model/stockfish/EvaluationResponse";

@Injectable({
    providedIn: 'root'
})
export class StockfishService {

    readonly PATH: string = `/gateway/stockfish/api/board`

    private http: HttpClient = inject(HttpClient)

    async evaluateBoard(boardId: string): Promise<EvaluationResponse>  {
        return firstValueFrom(
            this.http.get<EvaluationResponse>(`${this.PATH}/${boardId}/evaluate`)
        )
    }

    async establishBestMoveAndContinuation(boardId: string): Promise<BestMoveAndContinuationResponse> {
        return firstValueFrom(
            this.http.get<BestMoveAndContinuationResponse>(`${this.PATH}/${boardId}/best-move-and-continuation`)
        )
    }

}
