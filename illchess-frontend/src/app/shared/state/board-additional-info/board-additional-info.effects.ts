import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { from, map, switchMap } from "rxjs";
import { AcceptDrawRequest } from "../../model/game/AcceptDrawRequest";
import { BoardAdditionalInfoView } from "../../model/game/BoardAdditionalInfoView";
import { ProposeDrawRequest } from "../../model/game/ProposeDrawRequest";
import { RefreshBoardDto } from "../../model/game/RefreshBoardRequest";
import { RejectDrawRequest } from "../../model/game/RejectDrawRequest";
import { ResignGameRequest } from "../../model/game/ResignGameRequest";
import { GameService } from "../../service/GameService";
import { acceptDraw, acceptTakingBackMove, bestMoveAndContinuationLoaded, boardAdditionalInfoLoaded, establishBestMoveAndContinuation, establishEvaluation, evaluationLoaded, proposeDraw, proposeTakingBackMove, refreshAdditionalInfoOfBoard, rejectDraw, rejectTakingBackMove, resignGame } from "./board-additional-info.actions";
import { StockfishService } from "../../service/StockfishService";
import { EvaluationResponse } from "../../model/stockfish/EvaluationResponse";
import { BestMoveAndContinuationResponse } from "../../model/stockfish/BestMoveAndContinuationResponse";
import { ProposeTakingBackMoveRequest } from "../../model/game/ProposeTakingBackMoveRequest";
import { RejectTakingBackMoveRequest } from "../../model/game/RejectTakingBackMoveRequest";
import { AcceptTakingBackMoveRequest } from "../../model/game/AcceptTakingBackMoveRequest";

@Injectable({
    providedIn: 'root'
})
export class BoardAdditionalInfoEffects {

    constructor(
        private actions$: Actions,
        private chessBoardService: GameService,
        private chessBoardStockfishService: StockfishService
    ) { }

    refreshBoardAdditionalInfo$ = createEffect(
        () => this.actions$.pipe(
            ofType(refreshAdditionalInfoOfBoard),
            switchMap(
                (dto: RefreshBoardDto) => from(this.chessBoardService.refreshBoardAdditionalInfo(dto.boardId))
                    .pipe(
                        map((response: BoardAdditionalInfoView) => boardAdditionalInfoLoaded(response))
                    )
            )
        )
    )

    resignGame$ = createEffect(
        () => this.actions$.pipe(
            ofType(resignGame),
            switchMap(
                (dto: ResignGameRequest) => from(this.chessBoardService.resignGame(dto))
            )
        ),
        {
            dispatch:  false
        }
    )

    proposeDraw$ =  createEffect(
        () => this.actions$.pipe(
            ofType(proposeDraw),
            switchMap(
                (dto: ProposeDrawRequest) => from(this.chessBoardService.proposeDraw(dto))
            )
        ),
        {
            dispatch: false
        }
    )
    
    rejectDraw$ =  createEffect(
        () => this.actions$.pipe(
            ofType(rejectDraw),
            switchMap(
                (dto: RejectDrawRequest) => from(this.chessBoardService.rejectDraw(dto))
            )
        ),
        {
            dispatch: false
        }
    )

    acceptDraw$ =  createEffect(
        () => this.actions$.pipe(
            ofType(acceptDraw),
            switchMap(
                (dto: AcceptDrawRequest) => from(this.chessBoardService.acceptDraw(dto))
            )
        ),
        {
            dispatch: false
        }
    )

    proposeTakingBackMove$ = createEffect(
        () => this.actions$.pipe(
            ofType(proposeTakingBackMove),
            switchMap(
                (dto: ProposeTakingBackMoveRequest) => from(this.chessBoardService.proposeTakingBackMove(dto))
            )
        ),
        {
            dispatch: false
        }
    )

    rejectTakingBackMove$ = createEffect(
        () => this.actions$.pipe(
            ofType(rejectTakingBackMove),
            switchMap(
                (dto: RejectTakingBackMoveRequest) => from(this.chessBoardService.rejectTakingBackMove(dto))
            )
        ),
        {
            dispatch: false
        }
    )

    acceptTakingBackMove$ = createEffect(
        () => this.actions$.pipe(
            ofType(acceptTakingBackMove),
            switchMap(
                (dto: AcceptTakingBackMoveRequest) => from(this.chessBoardService.acceptTakingBackMove(dto))
            )
        ),
        {
            dispatch: false
        }
    )

    evaluateBoard$ = createEffect(
        () => this.actions$.pipe(
            ofType(establishEvaluation),
            switchMap(
                (dto: {boardId: string}) => from(this.chessBoardStockfishService.evaluateBoard(dto.boardId))
                    .pipe(
                        map((response: EvaluationResponse) => evaluationLoaded(response))
                    )
            )
        )
    )

    establishBestMoveAndContinuation$ = createEffect(
        () => this.actions$.pipe(
            ofType(establishBestMoveAndContinuation),
            switchMap(
                (dto: {boardId: string}) => from(this.chessBoardStockfishService.establishBestMoveAndContinuation(dto.boardId))
                    .pipe(
                        map((response: BestMoveAndContinuationResponse) => bestMoveAndContinuationLoaded(response))
                    )
            )
        )
    )

}