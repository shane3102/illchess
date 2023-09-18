package pl.illchess.application;

import pl.illchess.domain.chess_board.ChessBoard;

public interface SaveGameStateUseCase {

    ChessBoard saveChessBoard(ChessBoard chessBoard);
}
