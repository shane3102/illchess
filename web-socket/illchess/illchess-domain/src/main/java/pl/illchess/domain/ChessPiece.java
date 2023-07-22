package pl.illchess.domain;

import java.io.Serializable;

public class ChessPiece implements Serializable {
    private ChessPieceType chessPieceType;
    private ChessPieceColor chessPieceColor;

    public ChessPiece(ChessPieceType chessPieceType, ChessPieceColor chessPieceColor) {
        this.chessPieceType = chessPieceType;
        this.chessPieceColor = chessPieceColor;
    }
}
