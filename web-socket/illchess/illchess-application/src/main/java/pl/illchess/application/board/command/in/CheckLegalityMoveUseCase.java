package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.CheckLegalMoves;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;

import java.util.Set;
import java.util.UUID;

public interface CheckLegalityMoveUseCase {

    Set<Square> checkLegalityOfMove(MovePieceAttemptCmd cmd);

    record MovePieceAttemptCmd(
        UUID boardId,
        String startSquare,
        String pieceColor,
        String pieceType
    ) {
        public CheckLegalMoves toCommand() {
            PieceType pieceType = new PieceType(this.pieceType);
            PieceColor pieceColor = PieceColor.valueOf(this.pieceColor);
            Square startSquare = Square.valueOf(this.startSquare);

            return new CheckLegalMoves(
                new BoardId(boardId),
                PieceType.getPieceByPieceType(pieceType, pieceColor, startSquare)
            );
        }
    }
}
