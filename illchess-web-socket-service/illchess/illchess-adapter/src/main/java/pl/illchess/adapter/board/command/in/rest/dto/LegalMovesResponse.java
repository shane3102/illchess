package pl.illchess.adapter.board.command.in.rest.dto;

import pl.illchess.domain.board.model.square.Square;

import java.util.Set;

public record LegalMovesResponse(Set<Square> legalSquares) {
}
