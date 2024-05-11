package pl.illchess.stockfish.application.evaluation.command

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.illchess.stockfish.application.evaluation.command.`in`.EvaluateBoardUseCase
import pl.illchess.stockfish.application.evaluation.command.`in`.EvaluateBoardUseCase.EvaluateBoardCmd
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.application.position.command.out.LoadBoard
import pl.illchess.stockfish.domain.board.exception.BoardNotFoundException
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation
import pl.illchess.stockfish.domain.evaluation.exception.EvaluationNotEstablishedException


class EvaluateBoardService(
    private val loadBoard: LoadBoard,
    private val loadBoardEvaluation: LoadBoardEvaluation
) : EvaluateBoardUseCase {

    override fun evaluateBoard(cmd: EvaluateBoardCmd): Evaluation {
        log.info("Evaluating position on board with id = {}", cmd.boardId)
        val command = cmd.toCommand()
        val fenBoardPosition = loadBoard.loadBoard(command.boardId) ?: throw BoardNotFoundException(command.boardId)
        val result = loadBoardEvaluation.loadBoardEvaluation(fenBoardPosition) ?: throw EvaluationNotEstablishedException(command.boardId)
        log.info("Evaluated position board with id = {}. Result is {}", cmd.boardId, result.value)
        return result
    }

}

private val log: Logger = LoggerFactory.getLogger(EvaluateBoardService::class.java)
