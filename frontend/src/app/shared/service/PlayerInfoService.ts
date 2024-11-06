import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { GameFinishedView } from "../model/player-info/GameFinishedView";
import { firstValueFrom } from "rxjs";
import { Page } from "../model/player-info/Page";
import { GameSnippetView } from "../model/player-info/GameSnippetView";
import { PlayerView } from "../model/player-info/PlayerView";

@Injectable({
    providedIn: 'root'
})
export class PlayerInfoService {

    readonly PATH: string = `/gateway/player-info`

    private http: HttpClient = inject(HttpClient)

    async getFinishedGameById(gameId: string): Promise<GameFinishedView> {
        return firstValueFrom(this.http.get<GameFinishedView>(`${this.PATH}/api/game/${gameId}`))
    }

    async getLatestGamesPageable(pageNumber: number, pageSize: number): Promise<Page<GameSnippetView>> {
        let params = new HttpParams()
            .set('pageNumber', pageNumber)
            .set('pageSize', pageSize)
        return firstValueFrom(this.http.get<Page<GameSnippetView>>(`${this.PATH}/api/game/latest`, { params: params }))
    }

    async getPlayerRankingPageable(pageNumber: number, pageSize: number): Promise<Page<PlayerView>> {
        let params = new HttpParams()
            .set('pageNumber', pageNumber)
            .set('pageSize', pageSize)
        return firstValueFrom(this.http.get<Page<PlayerView>>(`${this.PATH}/api/player/ranking`, { params: params }))
    }

}