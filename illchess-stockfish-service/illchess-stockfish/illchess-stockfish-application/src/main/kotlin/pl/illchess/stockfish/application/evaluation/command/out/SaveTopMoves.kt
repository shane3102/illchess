package pl.illchess.stockfish.application.evaluation.command.out

import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves

interface SaveTopMoves {
    fun saveTopMoves(
        evaluationBoardInformation: EvaluationBoardInformation,
        topMoves: TopMoves
    )
}