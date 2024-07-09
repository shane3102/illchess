package pl.illchess.application.board.command.in


import pl.illchess.application.board.BoardSpecification

import static org.spockframework.util.Pair.of

class MovementOnPreMoveTest extends BoardSpecification {

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
                            // TODO to remove (?)
                            null,
                            null,
                            null,
                            username1
                    )
            )
        }

        movesFollowingPreMoves.forEach {
            movePieceUseCase.movePiece(
                    new MovePieceUseCase.MovePieceCmd(
                            boardId.uuid(),
                            it.first(),
                            it.second(),
                            // TODO to remove (?)
                            null,
                            null,
                            null,
                            username2
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
        __
        preMovesToPerform                                                                                | _
        [of("D2", "D4"), of("C2", "C4"), of("G1", "F3"), of("G2", "G3"), of("F1", "G2"), of("E1", "G1")] | _
        [of("E2", "E4"), of("G1", "F3"), of("F1", "B5")]                                                 | _
        [of("E2", "E4"), of("G1", "F3"), of("F1", "C4")]                                                 | _
        [of("E2", "E4"), of("G1", "F3"), of("F3", "E5"), of("B1", "C3")]                                 | _
        [of("D2", "D4"), of("C2", "C4"), of("C4", "B5"), of("D4", "D5")]                                 | _
        __
        movesFollowingPreMoves                                                           | _
        [of("E7", "E6"), of("F7", "F6"), of("G7", "G6"), of("H7", "H6"), of("D7", "D6")] | _
        [of("E7", "E5"), of("B8", "C6")]                                                 | _
        [of("E7", "E5"), of("B8", "C6")]                                                 | _
        [of("E7", "E5"), of("B8", "C6"), of("G8", "F6"), of("C6", "E5")]                 | _
        [of("C7", "C5"), of("B7", "B5"), of("G8", "F6")]                                 | _
    }
}
