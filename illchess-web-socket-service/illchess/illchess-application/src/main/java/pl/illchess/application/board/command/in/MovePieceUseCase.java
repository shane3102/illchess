package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.board.model.state.player.Username;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;

import java.util.UUID;

public interface MovePieceUseCase {

    void movePiece(MovePieceCmd cmd);

    record MovePieceCmd(
        UUID boardId,
        String startSquare,
        String targetSquare,
        String pawnPromotedToPieceType,
        String username
    ) {
        public MovePiece toCommand() {
            Square startSquare = Square.valueOf(this.startSquare);
            Square targetSquare = Square.valueOf(this.targetSquare);
            PieceType promotionPieceType = pawnPromotedToPieceType == null ? null : new PieceType(this.pawnPromotedToPieceType);

            return new MovePiece(
                new BoardId(boardId),
                startSquare,
                targetSquare,
                promotionPieceType,
                new Username(username)
            );
        }
    }
}
