import { Injectable } from "@angular/core";
import { InitializeBoardRequest } from "../model/InitializeBoardRequest";
import { MovePieceRequest } from "../model/MovePieceRequest";
import { HttpClient } from "@angular/common/http";
import { BoardLegalMovesResponse } from "../model/BoardLegalMovesResponse";
import { CheckLegalMovesRequest } from "../model/CheckLegalMovesRequest";
import { firstValueFrom } from "rxjs";
import { InitializedBoardResponse } from "../model/InitializedBoardResponse";
import { BoardView } from "../model/BoardView";
import { ActiveBoardsView } from "../model/ActiveBoardsView";

@Injectable({
    providedIn: 'root'
})
export class ChessBoardService {

    readonly PATH: string = "/api/board"

    constructor(private httpService: HttpClient) {
    }

    async initializeBoard(initializeBoardRequest: InitializeBoardRequest): Promise<InitializedBoardResponse> {
        return firstValueFrom(this.httpService.put<InitializedBoardResponse>(this.PATH + "/join-or-initialize", initializeBoardRequest))
    }

    async movePiece(movePieceRequest: MovePieceRequest): Promise<void> {
        return firstValueFrom(this.httpService.put<void>(this.PATH + "/move-piece", movePieceRequest))
    }

    async getLegalMoves(request: CheckLegalMovesRequest): Promise<BoardLegalMovesResponse> {
        return firstValueFrom(this.httpService.put<BoardLegalMovesResponse>(this.PATH + "/legal-moves", request))
    }

    async refreshBoard(boardId: string): Promise<BoardView> {
        return firstValueFrom(this.httpService.get<BoardView>(`${this.PATH}/refresh/${boardId}`))
    }

    async refreshActiveBoards(): Promise<ActiveBoardsView> {
        return firstValueFrom(this.httpService.get<ActiveBoardsView>(`${this.PATH}/active`))
    }

}