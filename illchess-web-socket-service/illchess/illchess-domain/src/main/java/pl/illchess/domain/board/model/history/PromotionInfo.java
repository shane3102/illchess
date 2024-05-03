package pl.illchess.domain.board.model.history;

import pl.illchess.domain.piece.model.info.PieceType;

public record PromotionInfo(PieceType targetPieceType) {
}
