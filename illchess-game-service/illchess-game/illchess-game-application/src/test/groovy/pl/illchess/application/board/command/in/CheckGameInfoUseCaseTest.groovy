package pl.illchess.application.board.command.in


import pl.illchess.application.board.GameSpecification
import pl.illchess.game.application.board.command.in.CheckBoardStateUseCase
import pl.illchess.game.application.board.command.in.JoinOrInitializeNewGameUseCase
import static pl.illchess.game.domain.game.model.state.GameResultCause.CHECKMATE
import static pl.illchess.game.domain.game.model.state.GameResultCause.INSUFFICIENT_MATERIAL
import static pl.illchess.game.domain.game.model.state.GameResultCause.STALEMATE
import static pl.illchess.game.domain.game.model.state.GameState.BLACK_WON
import static pl.illchess.game.domain.game.model.state.GameState.DRAW
import static pl.illchess.game.domain.game.model.state.GameState.WHITE_WON

class CheckGameInfoUseCaseTest extends GameSpecification {

    def "position should be correctly considered as checkmate or stalemate"() {
        given:
        def boardId = joinOrInitializeNewGameUseCase.joinOrInitializeNewGame(
                new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd("player1", fenString)
        )
        joinOrInitializeNewGameUseCase.joinOrInitializeNewGame(
                new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd("player2", fenString)
        )

        def cmd = new CheckBoardStateUseCase.CheckBoardStateCmd(boardId.uuid())
        when:
        checkIfCheckmateOrStalemateUseCase.checkBoardState(cmd)

        def loadedBoard = loadBoard.loadBoard(boardId).orElseThrow(AssertionError::new)

        then:
        loadedBoard.gameInfo().gameResult() == expectedResult
        loadedBoard.gameInfo().gameResultCause() == expectedGameResultCause

        where:
        fenString                                                    | expectedResult | expectedGameResultCause
        "8/8/pp6/k7/1PP5/K7/8/8 b"                                   | WHITE_WON      | CHECKMATE
        "6bk/8/6NK/8/8/8/8/8 b"                                      | WHITE_WON      | CHECKMATE
        "Nnb5/p1k1K3/2p5/Np6/8/8/8/8 b"                              | WHITE_WON      | CHECKMATE
        "8/1q6/4pp2/2K1kr2/3Ppb2/8/4b3/8 b"                          | WHITE_WON      | CHECKMATE
        "2n5/8/1b1pp3/1K1kr3/2Ppb/8/r7/7q b"                         | WHITE_WON      | CHECKMATE
        "R1B5/PPn1bK1q/3P4/8/8/1p6/p1pp3p/1k6 w"                     | BLACK_WON      | CHECKMATE
        "1r6/2q1r3/2pkp3/2p1p3/1p2p3/1N2P3/2P5/RNBQKB2 b"            | WHITE_WON      | CHECKMATE
        "8/8/4p3/3pP2B/b2P4/2P5/pB6/k1K2Q2 b"                        | WHITE_WON      | CHECKMATE
        "8/5n1K/5P2/8/5NPp/1P1PQ3/6kp/5b1q b"                        | WHITE_WON      | CHECKMATE
        "5bn1/4p1pb/4Qpkr/2P4p/7P/8/PPP1PPP1/RNB1KBNR b"             | DRAW           | STALEMATE
        "5bnr/4p1pq/4Qpkr/7p/7P/4P3/PPPP1PP1/RNB1KBNR b"             | DRAW           | STALEMATE
        "rn2k1nr/pp4pp/3p4/q1pP4/P1P2p1b/1b2pPRP/1P1NP1PQ/2B1KBNR w" | DRAW           | STALEMATE
        "8/8/8/2p2p1p/2P2P1k/4pP1P/4P1KP/5BNR w"                     | DRAW           | STALEMATE
        "1K6/8/1k6/pPp3p1/P1P2pP1/5P2/7P/8 b"                        | DRAW           | STALEMATE
        "3R1bk1/8/6K1/8/8/2B4P/8/8 b"                                | DRAW           | STALEMATE
        "1K6/8/8/8/8/8/8/1k6"                                        | DRAW           | INSUFFICIENT_MATERIAL
        "1K6/b7/8/8/8/8/8/1k6"                                       | DRAW           | INSUFFICIENT_MATERIAL
        "1K6/8/8/8/B7/8/8/1k6"                                       | DRAW           | INSUFFICIENT_MATERIAL
        "1K6/8/8/4n3/8/8/8/1k6"                                      | DRAW           | INSUFFICIENT_MATERIAL
        "1K6/8/8/8/8/1N6/8/1k6"                                      | DRAW           | INSUFFICIENT_MATERIAL
        "1K6/8/8/2N5/8/1N6/8/1k6"                                    | DRAW           | INSUFFICIENT_MATERIAL
        "1K6/nn6/8/8/8/8/8/1k6"                                      | DRAW           | INSUFFICIENT_MATERIAL
    }
}
