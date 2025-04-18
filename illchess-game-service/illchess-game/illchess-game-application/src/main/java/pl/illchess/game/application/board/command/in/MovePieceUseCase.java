package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.game.command.MovePiece;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.game.model.state.player.Username;
import pl.illchess.game.domain.piece.model.info.PieceType;

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
                new GameId(boardId),
                startSquare,
                targetSquare,
                promotionPieceType,
                new Username(username)
            );
        }
    }
}
