package pl.illchess.game.adapter.game.command.in.rest.dto;

import pl.illchess.game.domain.game.model.square.Square;

import java.util.Set;

public record LegalMovesResponse(Set<Square> legalSquares) {
}
