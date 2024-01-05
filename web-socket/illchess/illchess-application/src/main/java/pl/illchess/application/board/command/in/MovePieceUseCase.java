package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.Piece;
import pl.illchess.domain.piece.info.PieceColor;
import pl.illchess.domain.piece.info.PieceType;

import java.util.UUID;

public interface MovePieceUseCase {

    void movePiece(MovePieceCmd cmd);

    record MovePieceCmd(
            UUID boardId,
            String startSquare,
            String targetSquare,
            String pieceColor,
            String pieceType
    ) {
        public MovePiece toCommand() {
            return new MovePiece(
                    new BoardId(boardId),
                    new Piece(
                            PieceColor.valueOf(pieceColor),
                            PieceType.valueOf(pieceType)
                    ),
                    Square.valueOf(startSquare),
                    Square.valueOf(targetSquare)
            );
        }
    }
}
