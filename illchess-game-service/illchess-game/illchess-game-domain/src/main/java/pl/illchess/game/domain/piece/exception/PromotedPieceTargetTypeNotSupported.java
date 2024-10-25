package pl.illchess.game.domain.piece.exception;

import pl.illchess.game.domain.commons.exception.DomainException;
import pl.illchess.game.domain.piece.model.Piece;

import java.util.Set;

public class PromotedPieceTargetTypeNotSupported extends DomainException {

    public PromotedPieceTargetTypeNotSupported(Class<? extends Piece> promotingTypePiece, Set<Class<? extends Piece>> supportedPromotionTypes) {
        super("Promoting PAWN to piece type %s not supported. Supported piece types: %s".formatted(promotingTypePiece, supportedPromotionTypes));
    }

    public PromotedPieceTargetTypeNotSupported() {
        super("Promotion target piece type of PAWN was not specified");
    }
}
