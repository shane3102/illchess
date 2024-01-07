package pl.illchess.domain.board.model.square.info;

import pl.illchess.domain.board.model.square.SquaresConnectedContents;

import static pl.illchess.domain.board.model.square.SimpleSquare.*;

public enum SquareRank {
    _1(SquaresConnectedContents.of(A1, B1, C1, D1, E1, F1, G1, H1)),
    _2(SquaresConnectedContents.of(A2, B2, C2, D2, E2, F2, G2, H2)),
    _3(SquaresConnectedContents.of(A3, B3, C3, D3, E3, F3, G3, H3)),
    _4(SquaresConnectedContents.of(A4, B4, C4, D4, E4, F4, G4, H4)),
    _5(SquaresConnectedContents.of(A5, B5, C5, D5, E5, F5, G5, H5)),
    _6(SquaresConnectedContents.of(A6, B6, C6, D6, E6, F6, G6, H6)),
    _7(SquaresConnectedContents.of(A7, B7, C7, D7, E7, F7, G7, H7)),
    _8(SquaresConnectedContents.of(A8, B8, C8, D8, E8, F8, G8, H8));

    final SquaresConnectedContents containedSquares;

    SquareRank(SquaresConnectedContents containedSquares) {
        this.containedSquares = containedSquares;
    }

    public SquaresConnectedContents getContainedSquares() {
        return containedSquares;
    }
}
