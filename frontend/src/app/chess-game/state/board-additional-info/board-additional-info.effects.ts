import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { from, map, switchMap } from "rxjs";
import { AcceptDrawRequest } from "../../model/AcceptDrawRequest";
import { BoardAdditionalInfoView } from "../../model/BoardAdditionalInfoView";
import { ProposeDrawRequest } from "../../model/ProposeDrawRequest";
import { RefreshBoardDto } from "../../model/RefreshBoardRequest";
import { RejectDrawRequest } from "../../model/RejectDrawRequest";
import { ResignGameRequest } from "../../model/ResignGameRequest";
import { ChessBoardService } from "../../service/ChessBoardService";
import { acceptDraw, bestMoveAndContinuationLoaded, boardAdditionalInfoLoaded, establishBestMoveAndContinuation, establishEvaluation, evaluationLoaded, proposeDraw, refreshAdditionalInfoOfBoard, rejectDraw, resignGame } from "./board-additional-info.actions";
import { ChessBoardStockfishService } from "../../service/ChessBoardStockfishService";
import { EvaluationResponse } from "../../model/EvaluationResponse";
import { BestMoveAndContinuationResponse } from "../../model/BestMoveAndContinuationResponse";

@Injectable({
    providedIn: 'root'
})
export class BoardAdditionalInfoEffects {

    constructor(
        private actions$: Actions,
        private chessBoardService: ChessBoardService,
        private chessBoardStockfishService: ChessBoardStockfishService
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