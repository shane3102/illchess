package pl.illchess.application;

import pl.illchess.domain.ChessBoard;

public interface SaveChessBoardStateUseCase {

    ChessBoard saveChessBoard(ChessBoard chessBoard);
}
