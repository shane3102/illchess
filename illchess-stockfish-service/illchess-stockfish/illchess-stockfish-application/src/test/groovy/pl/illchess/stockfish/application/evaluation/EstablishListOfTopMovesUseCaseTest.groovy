package pl.illchess.stockfish.application.evaluation

import pl.illchess.stockfish.application.UnitTestSpecification
import pl.illchess.stockfish.application.evaluation.command.in.EstablishListOfTopMovesUseCase
import pl.illchess.stockfish.application.evaluation.command.in.EstablishListOfTopMovesUseCase.EstablishListOfTopMovesCmd
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation

class EstablishListOfTopMovesUseCaseTest extends UnitTestSpecification {

    EstablishListOfTopMovesUseCase useCase = evaluateBoardService

    def "should save top moves on calculation"() {
        given:
        def gameId = UUID.randomUUID()
        def moveCount = 3
        def depth = 1
        def evaluationBoardInformation = new EvaluationBoardInformation(defaultFenBoardPosition, depth)

        def cmd = new EstablishListOfTopMovesCmd(
                gameId,
                moveCount,
                depth
        )

        when:
        def topMoves = useCase.establishListOfTopMoves(cmd)

        then:
        topMoves.topMovesList.size() == moveCount
        1 * loadTopMoves.loadTopMoves(evaluationBoardInformation)
        1 * saveTopMoves.saveTopMoves(evaluationBoardInformation, _)
        1 * calculateTopMoves.calculateTopMoves(defaultFenBoardPosition, moveCount, depth)
    }

    def "should not calculate top moves if calculation already present"() {
        given:
        def gameId = UUID.randomUUID()
        def moveCount = 5
        def depth = 1
        def evaluationBoardInformation = new EvaluationBoardInformation(defaultFenBoardPosition, depth)

        def cmd = new EstablishListOfTopMovesCmd(
                gameId,
                moveCount,
                depth
        )

        when:
        useCase.establishListOfTopMoves(cmd)
        def topMoves = useCase.establishListOfTopMoves(cmd)

        then:
        topMoves.topMovesList.size() == moveCount
        2 * loadTopMoves.loadTopMoves(evaluationBoardInformation)
        1 * saveTopMoves.saveTopMoves(evaluationBoardInformation, _)
        1 * calculateTopMoves.calculateTopMoves(defaultFenBoardPosition, moveCount, depth)
    }

}
