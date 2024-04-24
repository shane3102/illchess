import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { AcceptDrawRequest } from "../model/AcceptDrawRequest";
import { ActiveBoardsView } from "../model/ActiveBoardsView";
import { BoardAdditionalInfoView } from "../model/BoardAdditionalInfoView";
import { BoardLegalMovesResponse } from "../model/BoardLegalMovesResponse";
import { BoardView } from "../model/BoardView";
import { CheckLegalMovesRequest } from "../model/CheckLegalMovesRequest";
import { InitializeBoardRequest } from "../model/InitializeBoardRequest";
import { InitializedBoardResponse } from "../model/InitializedBoardResponse";
import { MovePieceRequest } from "../model/MovePieceRequest";
import { ProposeDrawRequest } from "../model/ProposeDrawRequest";
import { RejectDrawRequest } from "../model/RejectDrawRequest";
import { ResignGameRequest } from "../model/ResignGameRequest";

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

    async refreshBoardAdditionalInfo(boardId: string): Promise<BoardAdditionalInfoView> {
        return firstValueFrom(this.httpService.get<BoardAdditionalInfoView>(`${this.PATH}/refresh/info/${boardId}`))
    }

    async resignGame(resignGame: ResignGameRequest): Promise<void> {
        return firstValueFrom(this.httpService.put<void>(`${this.PATH}/resign`, resignGame))
    }

    async proposeDraw(proposeDraw: ProposeDrawRequest): Promise<void> {
        return firstValueFrom(this.httpService.put<void>(`${this.PATH}/propose-draw`, proposeDraw));
    }

    async rejectDraw(rejectDraw: RejectDrawRequest): Promise<void> {
        return firstValueFrom(this.httpService.put<void>(`${this.PATH}/reject-draw`, rejectDraw));
    }

    async acceptDraw(acceptDraw: AcceptDrawRequest): Promise<void> {
        return firstValueFrom(this.httpService.put<void>(`${this.PATH}/accept-draw`, acceptDraw));
    }

}