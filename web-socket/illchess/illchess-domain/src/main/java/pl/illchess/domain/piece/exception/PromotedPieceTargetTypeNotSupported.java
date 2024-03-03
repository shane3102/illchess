package pl.illchess.domain.piece.exception;

import pl.illchess.domain.commons.exception.DomainException;
import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.type.Bishop;
import pl.illchess.domain.piece.model.type.Knight;
import pl.illchess.domain.piece.model.type.Queen;
import pl.illchess.domain.piece.model.type.Rook;

import java.util.Set;

public class PromotedPieceTargetTypeNotSupported extends DomainException {

    public PromotedPieceTargetTypeNotSupported(Class<? extends Piece> promotingTypePiece, Set<Class<? extends Piece>> supportedPromotionTypes) {
        super("Promoting PAWN to piece type %s not supported. Supported piece types: %s".formatted(promotingTypePiece, supportedPromotionTypes));
    }

    public PromotedPieceTargetTypeNotSupported() {
        super("Promotion target piece type of PAWN was not specified");
    }
}
