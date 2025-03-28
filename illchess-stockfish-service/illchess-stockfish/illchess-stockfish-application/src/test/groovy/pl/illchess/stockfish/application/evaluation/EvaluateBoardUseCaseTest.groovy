package pl.illchess.stockfish.application.evaluation

import pl.illchess.stockfish.application.UnitTestSpecification
import pl.illchess.stockfish.application.evaluation.command.in.EvaluateBoardUseCase
import pl.illchess.stockfish.application.evaluation.command.in.EvaluateBoardUseCase.EvaluateBoardCmd
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation

class EvaluateBoardUseCaseTest extends UnitTestSpecification {

    EvaluateBoardUseCase useCase = evaluateBoardService

    def "should save evaluation on calculation"() {
        given:
        def evaluationBoardInformation = new EvaluationBoardInformation(defaultFenBoardPosition, null)
        def boardId = UUID.randomUUID()

        when:
        useCase.evaluateBoard(new EvaluateBoardCmd(boardId))

        then:
        def evaluation = loadBoardEvaluation.loadBoardEvaluation(evaluationBoardInformation)

        evaluation != null
        1 * loadBoardEvaluation.loadBoardEvaluation(evaluationBoardInformation)
        1 * saveBoardEvaluation.saveBoardEvaluation(evaluationBoardInformation, _)
        1 * calculateBoardEvaluation.calculateBoardEvaluation(defaultFenBoardPosition)

    }

    def "should not calculate evaluation multiple times if calculation already present"() {
        given:
        def evaluationBoardInformation = new EvaluationBoardInformation(defaultFenBoardPosition, null)
        def boardId = UUID.randomUUID()

        when:
        useCase.evaluateBoard(new EvaluateBoardCmd(boardId))
        useCase.evaluateBoard(new EvaluateBoardCmd(boardId))

        then:
        def evaluation = loadBoardEvaluation.loadBoardEvaluation(evaluationBoardInformation)

        evaluation != null
        2 * loadBoardEvaluation.loadBoardEvaluation(evaluationBoardInformation)
        1 * saveBoardEvaluation.saveBoardEvaluation(evaluationBoardInformation, _)
        1 * calculateBoardEvaluation.calculateBoardEvaluation(defaultFenBoardPosition)

    }

}
