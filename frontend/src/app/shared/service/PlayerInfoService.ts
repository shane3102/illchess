import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { GameFinishedView } from "../model/GameFinishedView";
import { firstValueFrom } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class PlayerInfoService {

    readonly PATH: string = `/gateway/player-info/`

    private http: HttpClient = inject(HttpClient)


    async getFinishedGameById(gameId: string): Promise<GameFinishedView> {
        return firstValueFrom(this.http.get<GameFinishedView>(`${this.PATH}/api/game/${gameId}`))
    }
}