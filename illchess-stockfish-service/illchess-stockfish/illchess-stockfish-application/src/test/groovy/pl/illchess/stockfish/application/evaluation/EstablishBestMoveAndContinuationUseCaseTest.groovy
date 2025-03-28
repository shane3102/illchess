package pl.illchess.stockfish.application.evaluation

import pl.illchess.stockfish.application.UnitTestSpecification
import pl.illchess.stockfish.application.evaluation.command.in.EstablishBestMoveAndContinuationUseCase
import pl.illchess.stockfish.application.evaluation.command.in.EstablishBestMoveAndContinuationUseCase.EstablishBestMoveAndContinuationCmd
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation

class EstablishBestMoveAndContinuationUseCaseTest extends UnitTestSpecification {

    EstablishBestMoveAndContinuationUseCase useCase = evaluateBoardService

    def "should save new best moves on calculation"() {
        given:
        def depth = 1
        def cmd = new EstablishBestMoveAndContinuationCmd(UUID.randomUUID(), depth)
        def evaluationBoardInformation = new EvaluationBoardInformation(defaultFenBoardPosition, depth)

        when:
        def bestMoveAndContinuation = useCase.establishBestMoveAndContinuation(cmd)

        then:
        bestMoveAndContinuation != null
        1 * loadBestMoveAndContinuation.loadBestMoveAndContinuation(evaluationBoardInformation)
        1 * saveBestMoveAndContinuation.saveBestMoveAndContinuation(evaluationBoardInformation, _)
        1 * calculateBestMoveAndContinuation.calculateBestMoveAndContinuation(defaultFenBoardPosition, depth)
    }

    def "should not calculate best moves multiple times if calculation already present"() {
        given:
        def depth = 69
        def cmd = new EstablishBestMoveAndContinuationCmd(UUID.randomUUID(), depth)
        def evaluationBoardInformation = new EvaluationBoardInformation(defaultFenBoardPosition, depth)

        when:
        useCase.establishBestMoveAndContinuation(cmd)
        def bestMoveAndContinuation = useCase.establishBestMoveAndContinuation(cmd)

        then:
        bestMoveAndContinuation != null
        2 * loadBestMoveAndContinuation.loadBestMoveAndContinuation(evaluationBoardInformation)
        1 * saveBestMoveAndContinuation.saveBestMoveAndContinuation(evaluationBoardInformation, _)
        1 * calculateBestMoveAndContinuation.calculateBestMoveAndContinuation(defaultFenBoardPosition, depth)
    }

}
