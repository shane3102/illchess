package pl.illchess.application.board.command.in

import pl.illchess.application.board.GameSpecification
import pl.illchess.game.application.game.command.in.CheckLegalityMoveUseCase
import pl.illchess.game.application.game.command.in.JoinOrInitializeNewGameUseCase
import pl.illchess.game.domain.game.model.square.Square

import static pl.illchess.game.domain.game.model.square.Square.*
import static pl.illchess.game.domain.piece.model.info.PieceColor.WHITE

class KingMovementTest extends GameSpecification {

    def "check king movement in simple situation"() {
        given:
        def username = "player1"
        def boardId = joinOrInitializeNewGameUseCase.joinOrInitializeNewGame(
                new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd(username, fenString)
        )
        joinOrInitializeNewGameUseCase.joinOrInitializeNewGame(
                new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd("player2", fenString)
        )

        def cmd = new CheckLegalityMoveUseCase.MovePieceAttemptCmd(
                boardId.uuid(),
                kingStartSquare.toString(),
                pieceColor.toString(),
                username
        )
        when:
        def loadedLegalMovesOfKing = checkLegalityMoveUseCase.checkLegalityOfMove(cmd)

        then:
        loadedLegalMovesOfKing == expectedLegalMovesSet

        where:
        fenString                 | kingStartSquare | pieceColor | expectedLegalMovesSet
        // Attacking piece is NOT protected
        "k7/8/8/8/8/8/8/6qK w"    | H1              | WHITE      | [G1] as Set<Square>
        "k7/8/8/8/8/8/5qK/8 w"    | G2              | WHITE      | [H1, H3, F2] as Set<Square>
        "k7/8/8/8/8/8/8/6rK w"    | H1              | WHITE      | [G1, H2] as Set<Square>
        "k7/8/8/8/8/8/5rK/8 w"    | G2              | WHITE      | [H1, H3, F2, G3, G1] as Set<Square>
        "k7/8/8/8/8/8/6b1/7K w"   | H1              | WHITE      | [H2, G1, G2] as Set<Square>
        "k7/8/8/8/8/5b2/6K1/8 w"  | G2              | WHITE      | [F3, G3, H3, H2, G1, F1, F2] as Set<Square>
        "k7/8/8/8/8/6b1/6K1/8 w"  | G2              | WHITE      | [G3, G1, F1, H1, H3, F3] as Set<Square>
        "k7/8/8/8/6b1/8/6K1/8 w"  | G2              | WHITE      | [G3, G1, F1, H1, F2, H2] as Set<Square>
        "k7/8/8/8/8/8/6p1/7K w"   | H1              | WHITE      | [H2, G1, G2] as Set<Square>
        "k7/8/8/8/8/5p2/6K/8 w"   | G2              | WHITE      | [F3, G3, H3, H2, G1, F1, F2, H1] as Set<Square>
        // Attacking piece is protected
        "k7/8/8/8/8/8/4rqK/8 w"   | G2              | WHITE      | [H1, H3] as Set<Square>
        "k7/8/8/8/8/8/8/5rrK w"   | H1              | WHITE      | [H2] as Set<Square>
        "k7/8/8/8/8/8/4rrK/8 w"   | G2              | WHITE      | [H1, H3, G3, G1] as Set<Square>
        "k7/8/8/8/8/8/5rb1/7K w"  | H1              | WHITE      | [G1, H2] as Set<Square>
        "k7/8/8/8/8/4rb2/6K/8 w"  | G2              | WHITE      | [G3, H3, H2, G1, F1, F2] as Set<Square>
        "k7/8/8/8/8/8/5rp1/7K w"  | H1              | WHITE      | [H2, G1] as Set<Square>
        "k7/8/8/8/8/4rp2/6K/8 w"  | G2              | WHITE      | [G3, H3, H2, G1, F1, F2, H1] as Set<Square>
        // Piece is covering some (or all) king that king could move to
        "k7/8/8/8/8/8/q7/6K1 w"   | G1              | WHITE      | [F1, H1] as Set<Square>
        "k7/8/8/8/8/5q2/8/6K1 w"  | G1              | WHITE      | [H2] as Set<Square>
        "k7/8/8/8/8/8/r7/6K1 w"   | G1              | WHITE      | [F1, H1] as Set<Square>
        "k7/8/8/8/4b3/8/6K1/8 w"  | G2              | WHITE      | [G3, H3, H2, G1, F1, F2] as Set<Square>
        "k7/8/8/8/5pp1/8/6K1/8 w" | G2              | WHITE      | [H2, G1, F1, F2, H1] as Set<Square>
        "k7/8/8/8/8/6n1/6K1/8 w"  | G2              | WHITE      | [G3, F3, H3, F2, H2, G1] as Set<Square>
        "k7/8/8/8/6n1/8/6K1/8 w"  | G2              | WHITE      | [G3, F3, H3, F1, H1, G1] as Set<Square>

    }

    def "check if king can 'hide' behind piece"() {
        given:
        def username = "player1"
        def boardId = joinOrInitializeNewGameUseCase.joinOrInitializeNewGame(
                new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd(username, fenString)
        )
        joinOrInitializeNewGameUseCase.joinOrInitializeNewGame(
                new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd("player2", fenString)
        )

        def cmd = new CheckLegalityMoveUseCase.MovePieceAttemptCmd(
                boardId.uuid(),
                kingStartSquare.toString(),
                pieceColor.toString(),
                username
        )
        when:
        def loadedLegalMovesOfKing = checkLegalityMoveUseCase.checkLegalityOfMove(cmd)

        then:
        loadedLegalMovesOfKing == expectedLegalMovesSet

        where:
        fenString                  | kingStartSquare | pieceColor | expectedLegalMovesSet
        "k7/8/8/8/4q3/5N2/8/6K1 w" | G1              | WHITE      | [F1, F2, G2, H2, H1] as Set<Square>
        "k7/8/8/8/4q3/5p2/8/6K1 w" | G1              | WHITE      | [F1, F2, H2, H1] as Set<Square>
        "k7/8/8/8/4b3/5N2/8/6K1 w" | G1              | WHITE      | [F1, F2, G2, H2, H1] as Set<Square>
        "k7/8/8/8/4b3/5p2/8/6K1 w" | G1              | WHITE      | [F1, F2, H2, H1] as Set<Square>
        "k7/8/8/8/8/8/r3P3/6K1 w"  | G1              | WHITE      | [G2, H2, F1, H1, F2] as Set<Square>
    }

    def "check if can castle"() {
        given:
        def username = "player1"
        def boardId = joinOrInitializeNewGameUseCase.joinOrInitializeNewGame(
                new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd(username, fenString)
        )
        joinOrInitializeNewGameUseCase.joinOrInitializeNewGame(
                new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd("player2", fenString)
        )

        def cmd = new CheckLegalityMoveUseCase.MovePieceAttemptCmd(
                boardId.uuid(),
                E1.toString(),
                pieceColor.toString(),
                username
        )
        when:
        def loadedLegalMovesOfKing = checkLegalityMoveUseCase.checkLegalityOfMove(cmd)

        then:
        loadedLegalMovesOfKing.containsAll(availableCastleMoves)
        !loadedLegalMovesOfKing.containsAll(unavailableCasleMoves.isEmpty() ? [null] : unavailableCasleMoves)


        where:
        fenString                    | kingStartSquare | pieceColor | availableCastleMoves | unavailableCasleMoves
        "k7/8/8/8/8/8/8/R3K2R w"     | E1              | WHITE      | [C1, G1]             | []
        "k7/8/8/8/8/8/8/R3K1BR w"    | E1              | WHITE      | [C1]                 | [G1]
        "k7/8/8/8/8/8/8/RN2K2R w"    | E1              | WHITE      | [G1]                 | [C1]
        "k7/8/8/8/8/8/8/RN2K1NR w"   | E1              | WHITE      | []                   | [C1, G1]
        // rook interrupts
        "k1r5/8/8/8/8/8/8/R3K2R w"   | E1              | WHITE      | [G1]                 | [C1]
        "rk6/8/8/8/8/8/8/R3K2R w"    | E1              | WHITE      | [C1, G1]             | []
        "k1r5/8/8/8/8/8/8/R3K2R w"   | E1              | WHITE      | [G1]                 | [C1]
        "k5r1/8/8/8/8/8/8/R3K2R w"   | E1              | WHITE      | [C1]                 | [G1]
        "r1k4r/8/8/8/8/8/8/R3K2R w"  | E1              | WHITE      | [C1, G1]             | []
        "k1r3r1/8/8/8/8/8/8/R3K2R w" | E1              | WHITE      | []                   | [C1, G1]
        // bishop interrupts
        "k7/7b/8/8/8/8/8/R3K2R w"    | E1              | WHITE      | [C1, G1]             | []
        "k7/8/7b/8/8/8/8/R3K2R w"    | E1              | WHITE      | [G1]                 | [C1]
        "bk6/8/8/8/8/8/8/R3K2R w"    | E1              | WHITE      | [C1, G1]             | []
        "k7/b7/8/8/8/8/8/R3K2R w"    | E1              | WHITE      | [C1]                 | [G1]
        "k7/b7/7b/8/8/8/8/R3K2R w"   | E1              | WHITE      | []                   | [C1, G1]
        "k7/8/8/8/8/4b3/8/R3K2R w"   | E1              | WHITE      | []                   | [C1, G1]
        "k7/8/8/8/8/3b4/8/R3K2R w"   | E1              | WHITE      | []                   | [C1, G1]
        // queen interrupts
        "k1q5/8/8/8/8/8/8/R3K2R w"   | E1              | WHITE      | [G1]                 | [C1]
        "qk6/8/8/8/8/8/8/R3K2R w"    | E1              | WHITE      | [C1, G1]             | []
        "k1q5/8/8/8/8/8/8/R3K2R w"   | E1              | WHITE      | [G1]                 | [C1]
        "k5q1/8/8/8/8/8/8/R3K2R w"   | E1              | WHITE      | [C1]                 | [G1]
        "q1k4q/8/8/8/8/8/8/R3K2R w"  | E1              | WHITE      | [C1, G1]             | []
        "k1q3q1/8/8/8/8/8/8/R3K2R w" | E1              | WHITE      | []                   | [C1, G1]
        "k7/7q/8/8/8/8/8/R3K2R w"    | E1              | WHITE      | [C1, G1]             | []
        "k7/8/7q/8/8/8/8/R3K2R w"    | E1              | WHITE      | [G1]                 | [C1]
        "qk6/8/8/8/8/8/8/R3K2R w"    | E1              | WHITE      | [C1, G1]             | []
        "k7/q7/8/8/8/8/8/R3K2R w"    | E1              | WHITE      | [C1]                 | [G1]
        "k7/q7/7q/8/8/8/8/R3K2R w"   | E1              | WHITE      | []                   | [C1, G1]
        "k7/8/8/8/8/4q3/8/R3K2R w"   | E1              | WHITE      | []                   | [C1, G1]
        "k7/8/8/8/8/3q4/8/R3K2R w"   | E1              | WHITE      | []                   | [C1, G1]
        // knight interrupts
        "k7/8/8/8/8/8/3n4/R3K2R w"   | E1              | WHITE      | []                   | [C1, G1]
        "k7/8/8/8/8/1n6/8/R3K2R w"   | E1              | WHITE      | [G1]                 | [C1]
        "k7/8/8/8/8/8/2n5/R3K2R w"   | E1              | WHITE      | []                   | [C1, G1]
        "k7/8/8/8/8/8/5n2/R3K2R w"   | E1              | WHITE      | [G1]                 | [C1]
        "k7/8/8/8/8/6n1/8/R3K2R w"   | E1              | WHITE      | [C1]                 | [G1]
        // pawn interrupts
        "k7/8/8/8/8/8/7p/R3K2R w"    | E1              | WHITE      | [C1]                 | [G1]
        "k7/8/8/8/8/8/6p1/R3K2R w"   | E1              | WHITE      | [C1]                 | [G1]
        "k7/8/8/8/8/8/5p2/R3K2R w"   | E1              | WHITE      | []                   | [C1, G1]
        "k7/8/8/8/8/8/4p3/R3K2R w"   | E1              | WHITE      | []                   | [C1, G1]
        "k7/8/8/8/8/8/p7/R3K2R w"    | E1              | WHITE      | [C1, G1]             | []
        "k7/8/8/8/8/8/1p6/R3K2R w"   | E1              | WHITE      | [G1]                 | [C1]
        "k7/8/8/8/8/8/2p5/R3K2R w"   | E1              | WHITE      | [G1]                 | [C1]
        "k7/8/8/8/8/8/3p4/R3K2R w"   | E1              | WHITE      | []                   | [C1, G1]
    }
}
