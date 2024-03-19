import { Injectable } from "@angular/core";
import { InitializeBoardRequest } from "../model/InitializeBoardRequest";
import { MovePieceRequest } from "../model/MovePieceRequest";
import { HttpClient } from "@angular/common/http";
import { BoardLegalMovesResponse } from "../model/BoardLegalMovesResponse";
import { CheckLegalMovesRequest } from "../model/CheckLegalMovesRequest";
import { firstValueFrom } from "rxjs";
import { InitializedBoardResponse } from "../model/InitializedBoardResponse";
import { BoardView } from "../model/BoardView";
import { StompService } from "./StompService";
import { ChessGameState } from "../state/chess-game.state";
import { Store } from "@ngrx/store";
import { boardLoaded } from "../state/board/board.actions";

@Injectable({
    providedIn: 'root'
})
export class ChessBoardService {

    readonly PATH: string = "/api/board"

    constructor(
        private stompService: StompService,
        private store: Store<ChessGameState>,
        private httpService: HttpClient
    ) {
        // TODO TO JAKOŚ INNACZEJ PRZEZ EFEKTY
        // this.stompService.subscribe(
        //     "/chess-topic",
        //     (response: any) => {
        //         let boardView: BoardView = JSON.parse(response.body)
        //         this.store.dispatch(boardLoaded(boardView))
        //     }
        // )
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

}