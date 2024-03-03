import { Injectable } from "@angular/core";
import { StompService } from "./StompService";
import { InitializeBoardRequest } from "../model/InitializeBoardRequest";
import { MovePieceRequest } from "../model/MovePieceRequest";
import { BoardView } from "../model/BoardView";
import { IllegalMoveView } from "../model/IllegalMoveView";
import { boardLoaded, illegalMove } from "../state/board/board.actions";
import { Store } from "@ngrx/store";
import { ChessGameState } from "../state/chess-game.state";
import { HttpClient } from "@angular/common/http";
import { BoardLegalMovesResponse } from "../model/BoardLegalMovesResponse";
import { CheckLegalMovesRequest } from "../model/CheckLegalMovesRequest";
import { firstValueFrom } from "rxjs";

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
        this.stompService.subscribe(
            "/chess-topic",
            (response: any) => {
                let boardView: BoardView = JSON.parse(response.body)
                this.store.dispatch(boardLoaded(boardView))
            }
        )
        this.stompService.subscribe(
            "/illegal-move",
            (response: any) => {
                let illegalMoveView: IllegalMoveView = JSON.parse(response.body)
                this.store.dispatch(illegalMove(illegalMoveView))
            }
        )
    }

    async initializeBoard(initializeBoardRequest: InitializeBoardRequest): Promise<void> {
        this.stompService.stompClient!.send(
            '/app/board/create',
            {},
            JSON.stringify(initializeBoardRequest)
        )
    }

    async movePiece(movePieceRequest: MovePieceRequest): Promise<void> {
        this.stompService.stompClient!.send(
            '/app/board/move-piece',
            {},
            JSON.stringify(movePieceRequest)
        )
    }

    async getLegalMoves(request: CheckLegalMovesRequest): Promise<BoardLegalMovesResponse> {
        return firstValueFrom(this.httpService.put<BoardLegalMovesResponse>(this.PATH+"/legal-moves", request))
    }

}