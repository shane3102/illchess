import { Injectable } from "@angular/core";
import { StompService } from "./StompService";
import { InitializeBoardRequest } from "../model/InitializeBoardRequest";
import { MovePieceRequest } from "../model/MovePieceRequest";
import { BoardView } from "../model/BoardView";
import { IllegalMoveView } from "../model/IllegalMoveView";
import { boardLoaded, illegalMove } from "../state/board/board.actions";
import { Store } from "@ngrx/store";
import { ChessGameState } from "../state/chess-game.state";

@Injectable({
    providedIn: 'root'
})
export class ChessBoardService {

    constructor(
        private stompService: StompService,
        private store: Store<ChessGameState>
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

}