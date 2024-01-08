package pl.illchess.domain.board.model.square;

import pl.illchess.domain.board.model.square.info.SquareDiagonal;
import pl.illchess.domain.board.model.square.info.SquareFile;
import pl.illchess.domain.board.model.square.info.SquareRank;
import pl.illchess.domain.piece.model.type.King;
import pl.illchess.domain.piece.model.PieceBehaviour;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.illchess.domain.board.model.square.info.SquareDiagonal.*;
import static pl.illchess.domain.board.model.square.info.SquareFile.*;
import static pl.illchess.domain.board.model.square.info.SquareRank.*;

public enum Square {
    A8(A, _8, A8_XX, H1_A8), B8(B, _8, A7_B8, H2_B8), C8(C, _8, A6_C8, H3_C8), D8(D, _8, A5_D8, H4_D8), E8(E, _8, A4_E8, H5_E8), F8(F, _8, A3_F8, H6_F8), G8(G, _8, A2_G8, H7_G8), H8(H, _8, A1_H8, H8_XX),
    A7(A, _7, A7_B8, G1_A7), B7(B, _7, A6_C8, H1_A8), C7(C, _7, A5_D8, H2_B8), D7(D, _7, A4_E8, H3_C8), E7(E, _7, A3_F8, H4_D8), F7(F, _7, A2_G8, H5_E8), G7(G, _7, A1_H8, H6_F8), H7(H, _7, B1_H7, H7_G8),
    A6(A, _6, A6_C8, F1_A6), B6(B, _6, A5_D8, G1_A7), C6(C, _6, A4_E8, H1_A8), D6(D, _6, A3_F8, H2_B8), E6(E, _6, A2_G8, H3_C8), F6(F, _6, A1_H8, H4_D8), G6(G, _6, B1_H7, H5_E8), H6(H, _6, C1_H6, H6_F8),
    A5(A, _5, A5_D8, E1_A5), B5(B, _5, A4_E8, F1_A6), C5(C, _5, A3_F8, G1_A7), D5(D, _5, A2_G8, H1_A8), E5(E, _5, A1_H8, H2_B8), F5(F, _5, B1_H7, H3_C8), G5(G, _5, C1_H6, H4_D8), H5(H, _5, D1_H5, H5_E8),
    A4(A, _4, A4_E8, D1_A4), B4(B, _4, A3_F8, E1_A5), C4(C, _4, A2_G8, F1_A6), D4(D, _4, A1_H8, G1_A7), E4(E, _4, B1_H7, H1_A8), F4(F, _4, C1_H6, H2_B8), G4(G, _4, D1_H5, H3_C8), H4(H, _4, E1_H4, H4_D8),
    A3(A, _3, A3_F8, C1_A3), B3(B, _3, A2_G8, D1_A4), C3(C, _3, A1_H8, E1_A5), D3(D, _3, B1_H7, F1_A6), E3(E, _3, C1_H6, G1_A7), F3(F, _3, D1_H5, H1_A8), G3(G, _3, E1_H4, H2_B8), H3(H, _3, F1_H3, H3_C8),
    A2(A, _2, A2_G8, B1_A2), B2(B, _2, A1_H8, C1_A3), C2(C, _2, B1_H7, D1_A4), D2(D, _2, C1_H6, E1_A5), E2(E, _2, D1_H5, F1_A6), F2(F, _2, E1_H4, G1_A7), G2(G, _2, F1_H3, H1_A8), H2(H, _2, G1_H2, H2_B8),
    A1(A, _1, A1_H8, A1_XX), B1(B, _1, B1_H7, B1_A2), C1(C, _1, C1_H6, C1_A3), D1(D, _1, D1_H5, D1_A4), E1(E, _1, E1_H4, E1_A5), F1(F, _1, F1_H3, F1_A6), G1(G, _1, G1_H2, G1_A7), H1(H, _1, H1_XX, H1_A8);

    final SquareRank rank;
    final SquareFile file;
    final SquareDiagonal squareDiagonal1;
    final SquareDiagonal squareDiagonal2;

    Square(SquareFile file, SquareRank rank, SquareDiagonal squareDiagonal1, SquareDiagonal squareDiagonal2) {
        this.file = file;
        this.rank = rank;
        this.squareDiagonal1 = squareDiagonal1;
        this.squareDiagonal2 = squareDiagonal2;
    }

    public SquareRank getRank() {
        return rank;
    }

    public SquareFile getFile() {
        return file;
    }

    public SquareDiagonal getSquareDiagonal1() {
        return squareDiagonal1;
    }

    public SquareDiagonal getSquareDiagonal2() {
        return squareDiagonal2;
    }

    public boolean isHigher(Square compared) {
        return this.rank.getNumber() > compared.rank.getNumber();
    }

    public boolean isLower(Square compared) {
        return this.rank.getNumber() < compared.rank.getNumber();
    }
}
