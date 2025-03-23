package pl.illchess.stockfish.application.evaluation.command.out.facade

import pl.illchess.stockfish.application.evaluation.command.out.CalculateTopMoves
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.application.evaluation.command.out.SaveTopMoves
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.command.EstablishListOfTopMoves
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves
import pl.illchess.stockfish.domain.evaluation.exception.TopMovesCouldNotBeEstablished

class TopMovesFacade(
    private val loadTopMoves: LoadTopMoves,
    private val saveTopMoves: SaveTopMoves,
    private val calculateTopMoves: CalculateTopMoves
) {

    fun establishAndSaveIfUpdatedTopMoves(
        command: EstablishListOfTopMoves
    ): TopMoves {
        val evaluationBoardInformation = EvaluationBoardInformation(command.fenBoardPosition, command.depth)
        val savedTopMoves = loadTopMoves.loadTopMoves(evaluationBoardInformation)
        val result: TopMoves

        return if (savedTopMoves == null || savedTopMoves.topMovesList.size < command.moveCount) {
            result = calculateTopMoves
                .calculateTopMoves(
                    command.fenBoardPosition,
                    command.moveCount,
                    command.depth
                )
                ?: throw TopMovesCouldNotBeEstablished(command.boardId)
            saveTopMoves.saveTopMoves(evaluationBoardInformation, result)
            result
        } else {
            TopMoves(savedTopMoves.topMovesList.take(command.moveCount))
        }
    }
}