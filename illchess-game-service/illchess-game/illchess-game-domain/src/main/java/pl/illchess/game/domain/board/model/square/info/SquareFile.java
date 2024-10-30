package pl.illchess.game.domain.board.model.square.info;

import pl.illchess.game.domain.board.model.square.SquaresConnectedContents;

import static pl.illchess.game.domain.board.model.square.SimpleSquare.*;

public enum SquareFile {
    A(SquaresConnectedContents.of(A1, A2, A3, A4, A5, A6, A7, A8), 'A'),
    B(SquaresConnectedContents.of(B1, B2, B3, B4, B5, B6, B7, B8), 'B'),
    C(SquaresConnectedContents.of(C1, C2, C3, C4, C5, C6, C7, C8), 'C'),
    D(SquaresConnectedContents.of(D1, D2, D3, D4, D5, D6, D7, D8), 'D'),
    E(SquaresConnectedContents.of(E1, E2, E3, E4, E5, E6, E7, E8), 'E'),
    F(SquaresConnectedContents.of(F1, F2, F3, F4, F5, F6, F7, F8), 'F'),
    G(SquaresConnectedContents.of(G1, G2, G3, G4, G5, G6, G7, G8), 'G'),
    H(SquaresConnectedContents.of(H1, H2, H3, H4, H5, H6, H7, H8), 'H');

    final SquaresConnectedContents containedSquares;
    final char letter;

    SquareFile(SquaresConnectedContents containedSquares, char letter) {
        this.containedSquares = containedSquares;
        this.letter = letter;
    }

    public SquaresConnectedContents getContainedSquares() {
        return containedSquares;
    }
}