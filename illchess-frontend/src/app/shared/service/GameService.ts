import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { AcceptDrawRequest } from "../model/game/AcceptDrawRequest";
import { AcceptTakingBackMoveRequest } from "../model/game/AcceptTakingBackMoveRequest";
import { ActiveGamesView } from "../model/game/ActiveBoardsView";
import { GameAdditionalInfoView } from "../model/game/BoardAdditionalInfoView";
import { BoardLegalMovesResponse } from "../model/game/BoardLegalMovesResponse";
import { GameView } from "../model/game/BoardView";
import { CheckLegalMovesRequest } from "../model/game/CheckLegalMovesRequest";
import { InitializeBoardRequest } from "../model/game/InitializeBoardRequest";
import { InitializedBoardResponse } from "../model/game/InitializedBoardResponse";
import { MovePieceRequest } from "../model/game/MovePieceRequest";
import { ProposeDrawRequest } from "../model/game/ProposeDrawRequest";
import { ProposeTakingBackMoveRequest } from "../model/game/ProposeTakingBackMoveRequest";
import { RejectDrawRequest } from "../model/game/RejectDrawRequest";
import { RejectTakingBackMoveRequest } from "../model/game/RejectTakingBackMoveRequest";
import { ResignGameRequest } from "../model/game/ResignGameRequest";
import { QuitOccupiedBoardRequest } from "../model/game/QuitOccupiedBoardRequest";

@Injectable({
    providedIn: 'root'
})
export class GameService {

    readonly PATH: string = `/gateway/game/api/board`

    private httpService = inject(HttpClient)

    async initializeBoard(initializeBoardRequest: InitializeBoardRequest): Promise<InitializedBoardResponse> {
        return firstValueFrom(this.httpService.put<InitializedBoardResponse>(this.PATH + "/join-or-initialize", initializeBoardRequest))
    }

    async movePiece(movePieceRequest: MovePieceRequest): Promise<void> {
        return firstValueFrom(this.httpService.put<void>(this.PATH + "/move-piece", movePieceRequest))
    }

    async getLegalMoves(request: CheckLegalMovesRequest): Promise<BoardLegalMovesResponse> {
        return firstValueFrom(this.httpService.put<BoardLegalMovesResponse>(this.PATH + "/legal-moves", request))
    }

    async refreshBoard(boardId: string): Promise<GameView> {
        return firstValueFrom(this.httpService.get<GameView>(`${this.PATH}/refresh/${boardId}`))
    }

    async refreshBoardWithPremoves(boardId: string, username: string): Promise<GameView> {
        return firstValueFrom(this.httpService.get<GameView>(`${this.PATH}/refresh/pre-moves/${boardId}/${username}`))
    }

    async refreshActiveBoards(): Promise<ActiveGamesView> {
        return firstValueFrom(this.httpService.get<ActiveGamesView>(`${this.PATH}/active`))
    }

    async refreshBoardAdditionalInfo(boardId: string): Promise<GameAdditionalInfoView> {
        return firstValueFrom(this.httpService.get<GameAdditionalInfoView>(`${this.PATH}/refresh/info/${boardId}`))
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

    async proposeTakingBackMove(proposeTakingBackMove: ProposeTakingBackMoveRequest): Promise<void> {
        return firstValueFrom(this.httpService.put<void>(`${this.PATH}/propose-take-back-move`, proposeTakingBackMove))
    }

    async rejectTakingBackMove(rejectTakingBackMove: RejectTakingBackMoveRequest): Promise<void> {
        return firstValueFrom(this.httpService.put<void>(`${this.PATH}/reject-take-back-move`, rejectTakingBackMove))
    }

    async acceptTakingBackMove(acceptTakingBackMove: AcceptTakingBackMoveRequest): Promise<void> {
        return firstValueFrom(this.httpService.put<void>(`${this.PATH}/accept-take-back-move`, acceptTakingBackMove))
    }

    async quitNotYetStartedGame(quitOccupiedBoardRequest: QuitOccupiedBoardRequest): Promise<void> {
        return firstValueFrom(this.httpService.put<void>(`${this.PATH}/quit-not-yet-started`, quitOccupiedBoardRequest))
    }

}