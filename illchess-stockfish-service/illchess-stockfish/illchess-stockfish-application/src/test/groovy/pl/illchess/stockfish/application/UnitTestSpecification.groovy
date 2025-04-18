package pl.illchess.stockfish.application

import pl.illchess.stockfish.application.board.command.out.LoadGame
import pl.illchess.stockfish.application.evaluation.command.EvaluateBoardService
import pl.illchess.stockfish.application.evaluation.command.out.CalculateBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.CalculateBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.CalculateTopMoves
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.application.evaluation.command.out.SaveBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.SaveBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.SaveTopMoves
import pl.illchess.stockfish.application.evaluation.command.out.facade.TopMovesFacade
import pl.illchess.stockfish.application.test_impl.BoardCalculationsTestImpl
import pl.illchess.stockfish.application.test_impl.InMemoryBestMoveAndContinuationRepository
import pl.illchess.stockfish.application.test_impl.InMemoryBoardEvaluationRepository
import pl.illchess.stockfish.application.test_impl.InMemoryGameRepository
import pl.illchess.stockfish.application.test_impl.InMemoryTopMovesRepository
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import spock.lang.Specification

abstract class UnitTestSpecification extends Specification {

    def defaultFenBoardPosition = new FenBoardPosition(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR",
            "w",
            "KQkq",
            "-",
            "0",
            "1"
    )

    def inMemoryBoardRepository = new InMemoryGameRepository(defaultFenBoardPosition)
    def inMemoryBoardEvaluationTestImpl = new InMemoryBoardEvaluationRepository()
    def inMemoryBestMoveAndContinuationTestImpl = new InMemoryBestMoveAndContinuationRepository()
    def inMemoryTopMovesTestImpl = new InMemoryTopMovesRepository()
    def boardCalculationsTestImpl = new BoardCalculationsTestImpl()

    LoadGame loadBoard = inMemoryBoardRepository

    CalculateBoardEvaluation calculateBoardEvaluation = Spy(boardCalculationsTestImpl)
    CalculateBestMoveAndContinuation calculateBestMoveAndContinuation = Spy(boardCalculationsTestImpl)
    CalculateTopMoves calculateTopMoves = Spy(boardCalculationsTestImpl)

    LoadBoardEvaluation loadBoardEvaluation = Spy(inMemoryBoardEvaluationTestImpl)
    SaveBoardEvaluation saveBoardEvaluation = Spy(inMemoryBoardEvaluationTestImpl)
    LoadBestMoveAndContinuation loadBestMoveAndContinuation = Spy(inMemoryBestMoveAndContinuationTestImpl)
    SaveBestMoveAndContinuation saveBestMoveAndContinuation = Spy(inMemoryBestMoveAndContinuationTestImpl)
    LoadTopMoves loadTopMoves = Spy(inMemoryTopMovesTestImpl)
    SaveTopMoves saveTopMoves = Spy(inMemoryTopMovesTestImpl)

    def evaluateBoardService = new EvaluateBoardService(
            loadBoard,
            calculateBoardEvaluation,
            calculateBestMoveAndContinuation,
            loadBoardEvaluation,
            loadBestMoveAndContinuation,
            saveBoardEvaluation,
            saveBestMoveAndContinuation,
            new TopMovesFacade(
                    loadTopMoves,
                    saveTopMoves,
                    calculateTopMoves
            )
    )

}
