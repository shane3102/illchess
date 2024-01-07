package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.PieceBehaviour;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;

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
            PieceType pieceType = new PieceType(this.pieceType);
            PieceColor pieceColor = PieceColor.valueOf(this.pieceColor);
            Square startSquare = Square.valueOf(this.startSquare);
            Square targetSquare = Square.valueOf(this.targetSquare);

            return new MovePiece(
                    new BoardId(boardId),
                    PieceBehaviour.getPieceByPieceType(pieceType, pieceColor, startSquare),
                    targetSquare
            );
        }
    }
}
