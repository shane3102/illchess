package pl.illchess.application.board.command.in

import pl.illchess.application.board.BoardSpecification
import pl.illchess.domain.board.model.Board
import pl.illchess.domain.board.model.BoardId
import pl.illchess.domain.board.model.history.MoveHistory

import static pl.illchess.domain.board.model.state.GameState.CHECKMATE
import static pl.illchess.domain.board.model.state.GameState.STALEMATE
import static pl.illchess.domain.piece.model.info.PieceColor.BLACK
import static pl.illchess.domain.piece.model.info.PieceColor.WHITE

class CheckIfCheckmateOrStalemateUseCaseTest extends BoardSpecification {

    def "position should be correctly considered as checkmate or stalemate"() {
        given:
        def boardId = new BoardId(UUID.randomUUID())
        def board = new Board(boardId, fenString as String, new MoveHistory())
        saveBoard.saveBoard(board)

        def cmd = new CheckIfCheckmateOrStalemateUseCase.CheckIsCheckmateOrStaleMateCmd(boardId.uuid())
        when:
        checkIfCheckmateOrStalemateUseCase.checkIfCheckmateOrStalemate(cmd)

        def loadedBoard = loadBoard.loadBoard(boardId).orElseThrow(AssertionError::new)

        then:
        loadedBoard.boardState().gameState() == expectedStatus
        loadedBoard.boardState().victoriousPlayerColor() == expectedVictoriousPlayerColor

        where:
        fenString                                                    | expectedStatus | expectedVictoriousPlayerColor
        "8/8/pp6/k7/1PP5/K7/8/8 b"                                   | CHECKMATE      | WHITE
        "6bk/8/6NK/8/8/8/8/8 b"                                      | CHECKMATE      | WHITE
        "Nnb5/p1k1K3/2p5/Np6/8/8/8/8 b"                              | CHECKMATE      | WHITE
        "8/1q6/4pp2/2K1kr2/3Ppb2/8/4b3/8 b"                          | CHECKMATE      | WHITE
        "2n5/8/1b1pp3/1K1kr3/2Ppb/8/r7/7q b"                         | CHECKMATE      | WHITE
        "R1B5/PPn1bK1q/3P4/8/8/1p6/p1pp3p/1k6 w"                     | CHECKMATE      | BLACK
        "1r6/2q1r3/2pkp3/2p1p3/1p2p3/1N2P3/2P5/RNBQKB2 b"            | CHECKMATE      | WHITE
        "8/8/4p3/3pP2B/b2P4/2P5/pB6/k1K2Q2 b"                        | CHECKMATE      | WHITE
        "8/5n1K/5P2/8/5NPp/1P1PQ3/6kp/5b1q b"                        | CHECKMATE      | WHITE
        "5bn1/4p1pb/4Qpkr/2P4p/7P/8/PPP1PPP1/RNB1KBNR b"             | STALEMATE      | null
        "5bnr/4p1pq/4Qpkr/7p/7P/4P3/PPPP1PP1/RNB1KBNR b"             | STALEMATE      | null
        "rn2k1nr/pp4pp/3p4/q1pP4/P1P2p1b/1b2pPRP/1P1NP1PQ/2B1KBNR w" | STALEMATE      | null
        "8/8/8/2p2p1p/2P2P1k/4pP1P/4P1KP/5BNR w"                     | STALEMATE      | null
        "1K6/8/1k6/pPp3p1/P1P2pP1/5P2/7P/8 b"                        | STALEMATE      | null
        "3R1bk1/8/6K1/8/8/2B4P/8/8 b"                                | STALEMATE      | null
    }
}
