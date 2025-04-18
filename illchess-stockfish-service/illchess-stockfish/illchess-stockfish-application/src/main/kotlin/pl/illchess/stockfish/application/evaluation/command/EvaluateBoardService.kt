package pl.illchess.stockfish.application.evaluation.command

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.illchess.stockfish.application.board.command.out.LoadGame
import pl.illchess.stockfish.application.evaluation.command.`in`.EstablishBestMoveAndContinuationUseCase
import pl.illchess.stockfish.application.evaluation.command.`in`.EstablishBestMoveAndContinuationUseCase.EstablishBestMoveAndContinuationCmd
import pl.illchess.stockfish.application.evaluation.command.`in`.EstablishListOfTopMovesUseCase
import pl.illchess.stockfish.application.evaluation.command.`in`.EstablishListOfTopMovesUseCase.EstablishListOfTopMovesCmd
import pl.illchess.stockfish.application.evaluation.command.`in`.EvaluateBoardUseCase
import pl.illchess.stockfish.application.evaluation.command.`in`.EvaluateBoardUseCase.EvaluateBoardCmd
import pl.illchess.stockfish.application.evaluation.command.out.CalculateBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.CalculateBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.SaveBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.SaveBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.facade.TopMovesFacade
import pl.illchess.stockfish.domain.board.domain.GameId
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.board.exception.BoardNotFoundException
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves
import pl.illchess.stockfish.domain.evaluation.exception.BestMoveAndContinuationCouldNotBeEstablished
import pl.illchess.stockfish.domain.evaluation.exception.EvaluationNotEstablishedException


class EvaluateBoardService(
    private val loadGame: LoadGame,

    private val calculateBoardEvaluation: CalculateBoardEvaluation,
    private val calculateBestMoveAndContinuation: CalculateBestMoveAndContinuation,

    private val loadBoardEvaluation: LoadBoardEvaluation,
    private val loadBestMoveAndContinuation: LoadBestMoveAndContinuation,

    private val saveBoardEvaluation: SaveBoardEvaluation,
    private val saveBestMoveAndContinuation: SaveBestMoveAndContinuation,

    private val topMovesFacade: TopMovesFacade

) : EvaluateBoardUseCase, EstablishBestMoveAndContinuationUseCase, EstablishListOfTopMovesUseCase {

    override fun evaluateBoard(cmd: EvaluateBoardCmd): Evaluation {
        log.info("Evaluating position on board with id = {}", cmd.gameId)
        val command = cmd.toCommand()
        val fenBoardPosition = loadGame.loadGame(command.gameId) ?: throw BoardNotFoundException(command.gameId)
        val evaluationBoardInformation = EvaluationBoardInformation(fenBoardPosition)

        var result = loadBoardEvaluation.loadBoardEvaluation(evaluationBoardInformation)

        if (result == null) {
            result = calculateBoardEvaluation.calculateBoardEvaluation(fenBoardPosition)
                ?: throw EvaluationNotEstablishedException(command.gameId)
            saveBoardEvaluation.saveBoardEvaluation(evaluationBoardInformation, result)
        }

        log.info("Evaluated position board with id = {}. Result is {}", cmd.gameId, result.value)
        return result
    }

    override fun establishBestMoveAndContinuation(cmd: EstablishBestMoveAndContinuationCmd): BestMoveAndContinuation {
        log.info("Establishing best move and following continuation on board with id = {}", cmd.gameId)
        val command = cmd.toCommand()
        val fenBoardPosition = loadGame.loadGame(command.gameId) ?: throw BoardNotFoundException(command.gameId)
        val evaluationBoardInformation = EvaluationBoardInformation(fenBoardPosition, command.depth)

        var result = loadBestMoveAndContinuation.loadBestMoveAndContinuation(evaluationBoardInformation)

        if (result == null) {
            result = calculateBestMoveAndContinuation
                .calculateBestMoveAndContinuation(
                    fenBoardPosition,
                    command.depth
                )
                ?: throw BestMoveAndContinuationCouldNotBeEstablished(command.gameId)
            saveBestMoveAndContinuation.saveBestMoveAndContinuation(evaluationBoardInformation, result)
        }

        log.info(
            "Successfully established best move and continuation on board with id = {}. Best move is {} and best continuation is {}",
            cmd.gameId,
            result.bestMove,
            result.continuation
        )
        return result
    }

    override fun establishListOfTopMoves(cmd: EstablishListOfTopMovesCmd): TopMoves {
        log.info("Establishing list of best moves on board with id = {}", cmd.gameId)
        val gameId = GameId(cmd.gameId)
        val fenBoardPosition = loadGame.loadGame(gameId) ?: throw BoardNotFoundException(gameId)
        val command = cmd.toCommand(fenBoardPosition)
        val result = topMovesFacade.establishAndSaveIfUpdatedTopMoves(command)
        log.info(
            "Successfully established list of top moves on board with id = {}. Top moves in position are {}",
            cmd.gameId,
            result.topMovesList
        )
        return result
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(EvaluateBoardService::class.java)
    }
}
