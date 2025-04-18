package pl.illchess.game.domain.game.model.square;

import pl.illchess.game.domain.game.model.square.info.SquareDiagonal;
import pl.illchess.game.domain.game.model.square.info.SquareFile;
import pl.illchess.game.domain.game.model.square.info.SquareRank;

public enum Square {
    A8(SquareFile.A, SquareRank._8, SquareDiagonal.A8_XX, SquareDiagonal.H1_A8), B8(SquareFile.B, SquareRank._8, SquareDiagonal.A7_B8, SquareDiagonal.H2_B8), C8(SquareFile.C, SquareRank._8, SquareDiagonal.A6_C8, SquareDiagonal.H3_C8), D8(SquareFile.D, SquareRank._8, SquareDiagonal.A5_D8, SquareDiagonal.H4_D8), E8(SquareFile.E, SquareRank._8, SquareDiagonal.A4_E8, SquareDiagonal.H5_E8), F8(SquareFile.F, SquareRank._8, SquareDiagonal.A3_F8, SquareDiagonal.H6_F8), G8(SquareFile.G, SquareRank._8, SquareDiagonal.A2_G8, SquareDiagonal.H7_G8), H8(SquareFile.H, SquareRank._8, SquareDiagonal.A1_H8, SquareDiagonal.H8_XX),
    A7(SquareFile.A, SquareRank._7, SquareDiagonal.A7_B8, SquareDiagonal.G1_A7), B7(SquareFile.B, SquareRank._7, SquareDiagonal.A6_C8, SquareDiagonal.H1_A8), C7(SquareFile.C, SquareRank._7, SquareDiagonal.A5_D8, SquareDiagonal.H2_B8), D7(SquareFile.D, SquareRank._7, SquareDiagonal.A4_E8, SquareDiagonal.H3_C8), E7(SquareFile.E, SquareRank._7, SquareDiagonal.A3_F8, SquareDiagonal.H4_D8), F7(SquareFile.F, SquareRank._7, SquareDiagonal.A2_G8, SquareDiagonal.H5_E8), G7(SquareFile.G, SquareRank._7, SquareDiagonal.A1_H8, SquareDiagonal.H6_F8), H7(SquareFile.H, SquareRank._7, SquareDiagonal.B1_H7, SquareDiagonal.H7_G8),
    A6(SquareFile.A, SquareRank._6, SquareDiagonal.A6_C8, SquareDiagonal.F1_A6), B6(SquareFile.B, SquareRank._6, SquareDiagonal.A5_D8, SquareDiagonal.G1_A7), C6(SquareFile.C, SquareRank._6, SquareDiagonal.A4_E8, SquareDiagonal.H1_A8), D6(SquareFile.D, SquareRank._6, SquareDiagonal.A3_F8, SquareDiagonal.H2_B8), E6(SquareFile.E, SquareRank._6, SquareDiagonal.A2_G8, SquareDiagonal.H3_C8), F6(SquareFile.F, SquareRank._6, SquareDiagonal.A1_H8, SquareDiagonal.H4_D8), G6(SquareFile.G, SquareRank._6, SquareDiagonal.B1_H7, SquareDiagonal.H5_E8), H6(SquareFile.H, SquareRank._6, SquareDiagonal.C1_H6, SquareDiagonal.H6_F8),
    A5(SquareFile.A, SquareRank._5, SquareDiagonal.A5_D8, SquareDiagonal.E1_A5), B5(SquareFile.B, SquareRank._5, SquareDiagonal.A4_E8, SquareDiagonal.F1_A6), C5(SquareFile.C, SquareRank._5, SquareDiagonal.A3_F8, SquareDiagonal.G1_A7), D5(SquareFile.D, SquareRank._5, SquareDiagonal.A2_G8, SquareDiagonal.H1_A8), E5(SquareFile.E, SquareRank._5, SquareDiagonal.A1_H8, SquareDiagonal.H2_B8), F5(SquareFile.F, SquareRank._5, SquareDiagonal.B1_H7, SquareDiagonal.H3_C8), G5(SquareFile.G, SquareRank._5, SquareDiagonal.C1_H6, SquareDiagonal.H4_D8), H5(SquareFile.H, SquareRank._5, SquareDiagonal.D1_H5, SquareDiagonal.H5_E8),
    A4(SquareFile.A, SquareRank._4, SquareDiagonal.A4_E8, SquareDiagonal.D1_A4), B4(SquareFile.B, SquareRank._4, SquareDiagonal.A3_F8, SquareDiagonal.E1_A5), C4(SquareFile.C, SquareRank._4, SquareDiagonal.A2_G8, SquareDiagonal.F1_A6), D4(SquareFile.D, SquareRank._4, SquareDiagonal.A1_H8, SquareDiagonal.G1_A7), E4(SquareFile.E, SquareRank._4, SquareDiagonal.B1_H7, SquareDiagonal.H1_A8), F4(SquareFile.F, SquareRank._4, SquareDiagonal.C1_H6, SquareDiagonal.H2_B8), G4(SquareFile.G, SquareRank._4, SquareDiagonal.D1_H5, SquareDiagonal.H3_C8), H4(SquareFile.H, SquareRank._4, SquareDiagonal.E1_H4, SquareDiagonal.H4_D8),
    A3(SquareFile.A, SquareRank._3, SquareDiagonal.A3_F8, SquareDiagonal.C1_A3), B3(SquareFile.B, SquareRank._3, SquareDiagonal.A2_G8, SquareDiagonal.D1_A4), C3(SquareFile.C, SquareRank._3, SquareDiagonal.A1_H8, SquareDiagonal.E1_A5), D3(SquareFile.D, SquareRank._3, SquareDiagonal.B1_H7, SquareDiagonal.F1_A6), E3(SquareFile.E, SquareRank._3, SquareDiagonal.C1_H6, SquareDiagonal.G1_A7), F3(SquareFile.F, SquareRank._3, SquareDiagonal.D1_H5, SquareDiagonal.H1_A8), G3(SquareFile.G, SquareRank._3, SquareDiagonal.E1_H4, SquareDiagonal.H2_B8), H3(SquareFile.H, SquareRank._3, SquareDiagonal.F1_H3, SquareDiagonal.H3_C8),
    A2(SquareFile.A, SquareRank._2, SquareDiagonal.A2_G8, SquareDiagonal.B1_A2), B2(SquareFile.B, SquareRank._2, SquareDiagonal.A1_H8, SquareDiagonal.C1_A3), C2(SquareFile.C, SquareRank._2, SquareDiagonal.B1_H7, SquareDiagonal.D1_A4), D2(SquareFile.D, SquareRank._2, SquareDiagonal.C1_H6, SquareDiagonal.E1_A5), E2(SquareFile.E, SquareRank._2, SquareDiagonal.D1_H5, SquareDiagonal.F1_A6), F2(SquareFile.F, SquareRank._2, SquareDiagonal.E1_H4, SquareDiagonal.G1_A7), G2(SquareFile.G, SquareRank._2, SquareDiagonal.F1_H3, SquareDiagonal.H1_A8), H2(SquareFile.H, SquareRank._2, SquareDiagonal.G1_H2, SquareDiagonal.H2_B8),
    A1(SquareFile.A, SquareRank._1, SquareDiagonal.A1_H8, SquareDiagonal.A1_XX), B1(SquareFile.B, SquareRank._1, SquareDiagonal.B1_H7, SquareDiagonal.B1_A2), C1(SquareFile.C, SquareRank._1, SquareDiagonal.C1_H6, SquareDiagonal.C1_A3), D1(SquareFile.D, SquareRank._1, SquareDiagonal.D1_H5, SquareDiagonal.D1_A4), E1(SquareFile.E, SquareRank._1, SquareDiagonal.E1_H4, SquareDiagonal.E1_A5), F1(SquareFile.F, SquareRank._1, SquareDiagonal.F1_H3, SquareDiagonal.F1_A6), G1(SquareFile.G, SquareRank._1, SquareDiagonal.G1_H2, SquareDiagonal.G1_A7), H1(SquareFile.H, SquareRank._1, SquareDiagonal.H1_XX, SquareDiagonal.H1_A8);

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

    public SimpleSquare toSimple() {
        return SimpleSquare.valueOf(this.name());
    }

    public static Square fromRankAndFile(char file, int rank) {
        return Square.valueOf(file + "" + rank);
    }
}
