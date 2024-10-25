package pl.illchess.game.adapter.board.command.in.rest.dto;

import pl.illchess.game.domain.board.model.square.Square;

import java.util.Set;

public record LegalMovesResponse(Set<Square> legalSquares) {
}
