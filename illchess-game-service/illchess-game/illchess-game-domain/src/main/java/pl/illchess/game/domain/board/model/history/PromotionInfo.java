package pl.illchess.game.domain.board.model.history;

import pl.illchess.game.domain.piece.model.info.PieceType;

public record PromotionInfo(PieceType targetPieceType) {
}
