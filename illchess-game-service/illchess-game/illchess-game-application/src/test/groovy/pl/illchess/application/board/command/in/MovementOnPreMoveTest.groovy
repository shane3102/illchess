package pl.illchess.application.board.command.in


import pl.illchess.application.board.GameSpecification
import pl.illchess.game.application.game.command.in.EstablishFenStringOfBoardUseCase
import pl.illchess.game.application.game.command.in.JoinOrInitializeNewGameUseCase
import pl.illchess.game.application.game.command.in.MovePieceUseCase

import java.util.stream.Stream

import static org.spockframework.util.Pair.of
import static pl.illchess.game.domain.piece.model.info.PieceColor.BLACK
import static pl.illchess.game.domain.piece.model.info.PieceColor.WHITE

class MovementOnPreMoveTest extends GameSpecification {

    def "basic pre-move cases"() {
        given:
        def username1 = "player1"
        def username2 = "player2"
        def boardId = joinOrInitializeNewGameUseCase.joinOrInitializeNewGame(
                new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd(username1, startPosition)
        )
        joinOrInitializeNewGameUseCase.joinOrInitializeNewGame(
                new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd(username2, startPosition)
        )

        when:
        preMovesToPerform.forEach {
            movePieceUseCase.movePiece(
                    new MovePieceUseCase.MovePieceCmd(
                            boardId.uuid(),
                            it.first(),
                            it.second(),
                            null,
                            WHITE == preMovingPlayer ? username1 : username2
                    )
            )
        }

        movesFollowingPreMoves.forEach {
            movePieceUseCase.movePiece(
                    new MovePieceUseCase.MovePieceCmd(
                            boardId.uuid(),
                            it.first(),
                            it.second(),
                            null,
                            WHITE == preMovingPlayer ? username2 : username1
                    )
            )
        }

        then:
        def fenBoardString = establishFenStringOfBoardUseCase.establishCurrentFenBoardString(new EstablishFenStringOfBoardUseCase.EstablishFenStringOfBoardCmd(boardId.uuid()))

        fenBoardString.position() == expectedEndingPosition.split(" ")[0]
        fenBoardString.activeColor() == expectedEndingPosition.split(" ")[1]

        where:
        startPosition                                   | expectedEndingPosition
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w" | "rnbqkbnr/ppp5/3ppppp/8/2PP4/5NP1/PP2PPBP/RNBQ1RK1 b"
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w" | "r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b"
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w" | "r1bqkbnr/pppp1ppp/2n5/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R b"
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w" | "r1bqkb1r/pppp1ppp/5n2/4n3/4P3/2N5/PPPP1PPP/R1BQKB1R w"
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w" | "rnbqkb1r/p2ppppp/5n2/1PpP4/8/8/PP2PPPP/RNBQKBNR b"
        "5k2/1p3pp1/2p5/p1P5/P2p4/2q5/2B5/1K6 w"        | "8/1p3pp1/2p5/p1P5/P2p4/2k5/Kq6/1B6 w"
        "4k3/8/5p2/3P3p/1NN5/1p6/1P12P2/2B3K1 b"        | "3k4/8/5p2/3P3K/2N5/8/1PN2P2/2B5 b"
        "5bk1/5ppp/1n6/8/5P1P/2P3P1/2N3K1/q7 b"         | "6k1/5pPp/5P1K/2b2n2/6q1/8/8/8 w"
        __
        preMovesToPerform                                                                                                                                                                | preMovingPlayer
        [of("D2", "D4"), of("C2", "C4"), of("G1", "F3"), of("G2", "G3"), of("F1", "G2"), of("E1", "G1")]                                                                                 | WHITE
        [of("E2", "E4"), of("G1", "F3"), of("F1", "B5")]                                                                                                                                 | WHITE
        [of("E2", "E4"), of("G1", "F3"), of("F1", "C4")]                                                                                                                                 | WHITE
        [of("E2", "E4"), of("G1", "F3"), of("F3", "E5"), of("B1", "C3")]                                                                                                                 | WHITE
        [of("D2", "D4"), of("C2", "C4"), of("C4", "B5"), of("D4", "D5")]                                                                                                                 | WHITE
        [of("F8", "E7"), of("E7", "F6"), of("F6", "G5"), of("G5", "F4"), of("F4", "E3"), of("E3", "D2"), of("C3", "B4"), of("D2", "C3"), of("B4", "B2")]                                 | BLACK
        [of("B4", "D3"), of("D3", "C5"), of("C5", "B3"), of("B3", "D4"), of("D4", "C2"), of("G1", "H2"), of("H2", "H3"), of("H3", "H4"), of("H4", "H5")]                                 | WHITE
        [of("A1", "B2"), of("B2", "C2"), of("C2", "C3"), of("B6", "C4"), of("F8", "C5"), of("C4", "E3"), of("C3", "C1"), of("C1", "G1"), of("G1", "G2"), of("G2", "G4"), of("E3", "F5")] | BLACK
        __
        movesFollowingPreMoves                                                                                                                                           | _
        [of("E7", "E6"), of("F7", "F6"), of("G7", "G6"), of("H7", "H6"), of("D7", "D6")]                                                                                 | _
        [of("E7", "E5"), of("B8", "C6")]                                                                                                                                 | _
        [of("E7", "E5"), of("B8", "C6")]                                                                                                                                 | _
        [of("E7", "E5"), of("B8", "C6"), of("G8", "F6"), of("C6", "E5")]                                                                                                 | _
        [of("C7", "C5"), of("B7", "B5"), of("G8", "F6")]                                                                                                                 | _
        [of("C2", "D1"), of("D1", "E2"), of("E2", "F1"), of("B1", "A2"), of("F1", "E2"), of("E2", "F1"), of("F1", "H3"), of("H3", "F5"), of("F5", "B1")]                 | _
        [of("E8", "D7"), of("D7", "E7"), of("E7", "D8"), of("D8", "C8"), of("C8", "D8"), of("D8", "D7"), of("D7", "D8"), of("D8", "C8"), of("C8", "D8")]                 | _
        [of("G2", "H3"), of("H3", "G4"), of("G4", "G5"), of("H4", "H5"), of("F4", "F5"), of("H5", "H6"), of("G3", "G4"), of("H6", "G7"), of("G5", "H6"), of("F5", "F6")] | _
    }

    def "pre-moves interrupted by enemy move"() {
        given:
        def username1 = "player1"
        def username2 = "player2"
        def boardId = joinOrInitializeNewGameUseCase.joinOrInitializeNewGame(
                new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd(username1, startPosition)
        )
        joinOrInitializeNewGameUseCase.joinOrInitializeNewGame(
                new JoinOrInitializeNewGameUseCase.JoinOrInitializeNewGameCmd(username2, startPosition)
        )

        when:
        Stream.concat(preMovesPerformed.stream(), preMovesIgnored.stream()).forEach {
            movePieceUseCase.movePiece(
                    new MovePieceUseCase.MovePieceCmd(
                            boardId.uuid(),
                            it.first(),
                            it.second(),
                            null,
                            WHITE == preMovingPlayer ? username1 : username2
                    )
            )
        }

        movesFollowingPreMoves.forEach {
            movePieceUseCase.movePiece(
                    new MovePieceUseCase.MovePieceCmd(
                            boardId.uuid(),
                            it.first(),
                            it.second(),
                            null,
                            WHITE == preMovingPlayer ? username2 : username1
                    )
            )
        }

        then:
        def fenBoardString = establishFenStringOfBoardUseCase.establishCurrentFenBoardString(new EstablishFenStringOfBoardUseCase.EstablishFenStringOfBoardCmd(boardId.uuid()))

        fenBoardString.position() == expectedEndingPosition.split(" ")[0]
        fenBoardString.activeColor() == expectedEndingPosition.split(" ")[1]

        def board = loadBoard.loadBoard(boardId).orElseThrow(AssertionError::new)
        preMovesPerformed.every {
            board.moveHistory().moveStack().any(movePerformed -> {
                movePerformed.startSquare().toString() == it.first()
                movePerformed.targetSquare().toString() == it.second()
            })
        }
        !preMovesIgnored.any {
            board.moveHistory().moveStack().any(movePerformed -> {
                movePerformed.startSquare().toString() == it.first()
                movePerformed.targetSquare().toString() == it.second()
            })
        }
        movesFollowingPreMoves.every {
            board.moveHistory().moveStack().any(movePerformed -> {
                movePerformed.startSquare().toString() == it.first()
                movePerformed.targetSquare().toString() == it.second()
            })
        }

        where:
        startPosition                                      | expectedEndingPosition                            | preMovingPlayer
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w"    | "rnbqkb1r/pppPpppp/5n2/8/8/8/PPPP1PPP/RNBQKBNR b" | BLACK
        "5b1r/p1p4p/B4p2/1r4nk/3P2n1/8/PP1N1PPP/RNBQK2R w" | "5b1r/prp4p/5p2/6nk/3P2n1/8/PP1N1PPP/RNBQK2R w"   | WHITE
        __
        preMovesPerformed                                | preMovesIgnored
        [of("G8", "F6"), of("F6", "G8"), of("G8", "F6")] | [of("B7", "B5"), of("A7", "A5")]
        [of("A6", "B7")]                                 | [of("B7", "F3")]
        __
        movesFollowingPreMoves                                           | _
        [of("E2", "E4"), of("E4", "E5"), of("E5", "E6"), of("E6", "D7")] | _
        [of("B5", "B7")]                                                 | _

    }
}
