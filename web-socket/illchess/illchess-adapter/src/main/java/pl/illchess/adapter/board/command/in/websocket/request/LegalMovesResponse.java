package pl.illchess.adapter.board.command.in.websocket.request;

import pl.illchess.domain.board.model.square.Square;

import java.util.Set;

public record LegalMovesResponse(Set<Square> legalSquares) {
}
