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
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w" | "rnbqkbnr/ppp5/3ppppp/8/2PP4/5NP1/PP2PPBP/RNBQK2R w"
        __
        preMovesToPerform                                                                | _
        [of("D2", "D4"), of("C2", "C4"), of("G1", "F3"), of("G2", "G3"), of("F1", "G2")] | _
        __
        movesFollowingPreMoves                                                           | _
        [of("E7", "E6"), of("F7", "F6"), of("G7", "G6"), of("H7", "H6"), of("D7", "D6")] | _
    }
}
