package pl.illchess.domain.chess_board;

import java.io.Serializable;
import java.util.Map;

public class ChessBoard implements Serializable {

    private ChessBoardId chessBoardId;

    private Map<ChessSquare, ChessPiece> chessBoardState;

    public void setChessBoardId(ChessBoardId chessBoardId) {
        this.chessBoardId = chessBoardId;
    }

    public ChessBoardId getChessBoardId() {
        return chessBoardId;
    }

    public ChessBoard(Map<ChessSquare, ChessPiece> chessBoardState) {
        this.chessBoardState = chessBoardState;
    }
}
