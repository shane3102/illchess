package pl.illchess.domain.board.model.square.info;

import pl.illchess.domain.board.model.square.SquaresConnectedContents;

import static pl.illchess.domain.board.model.square.SimpleSquare.*;

public enum SquareDiagonal {
    A8_XX(SquaresConnectedContents.of(A8)),
    A7_B8(SquaresConnectedContents.of(A7, B8)),
    A6_C8(SquaresConnectedContents.of(A6, B7, C8)),
    A5_D8(SquaresConnectedContents.of(A5, B6, C7, D8)),
    A4_E8(SquaresConnectedContents.of(A4, B5, C6, D7, E8)),
    A3_F8(SquaresConnectedContents.of(A3, B4, C5, D6, E7, F8)),
    A2_G8(SquaresConnectedContents.of(A2, B3, C4, D5, E6, F7, G8)),
    A1_H8(SquaresConnectedContents.of(A1, B2, C3, D4, E5, F6, G7, H8)),
    B1_H7(SquaresConnectedContents.of(B1, C2, D3, E4, F5, G6, H8)),
    C1_H6(SquaresConnectedContents.of(C1, D2, E3, F4, G5, H6)),
    D1_H5(SquaresConnectedContents.of(D1, E2, F3, G4, H5)),
    E1_H4(SquaresConnectedContents.of(E1, F2, G3, H4)),
    F1_H3(SquaresConnectedContents.of(F1, G2, H3)),
    G1_H2(SquaresConnectedContents.of(G1, H2)),
    H1_XX(SquaresConnectedContents.of(H1)),

    H8_XX(SquaresConnectedContents.of(H8)),
    H7_G8(SquaresConnectedContents.of(H7, G8)),
    H6_F8(SquaresConnectedContents.of(H6, G7, F8)),
    H5_E8(SquaresConnectedContents.of(H5, G6, F7, E8)),
    H4_D8(SquaresConnectedContents.of(H4, G5, F6, E7, D8)),
    H3_C8(SquaresConnectedContents.of(H3, G4, F5, E6, D7, C8)),
    H2_B8(SquaresConnectedContents.of(H2, G3, F4, E5, D6, C7, B8)),
    H1_A8(SquaresConnectedContents.of(H1, G2, F3, E4, D5, C6, B7, A8)),
    G1_A7(SquaresConnectedContents.of(G1, F2, E3, D4, C5, B6, A7)),
    F1_A6(SquaresConnectedContents.of(F1, E2, D3, C4, B5, A6)),
    E1_A5(SquaresConnectedContents.of(E1, D2, C3, B4, A5)),
    D1_A4(SquaresConnectedContents.of(D1, C2, B3, A4)),
    C1_A3(SquaresConnectedContents.of(C1, B2, A3)),
    B1_A2(SquaresConnectedContents.of(B1, A2)),
    A1_XX(SquaresConnectedContents.of(A1));


    final SquaresConnectedContents containedSquares;

    SquareDiagonal(SquaresConnectedContents containedSquares) {
        this.containedSquares = containedSquares;
    }

    public SquaresConnectedContents getContainedSquares() {
        return containedSquares;
    }
}
