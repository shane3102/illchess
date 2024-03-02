package pl.illchess.domain.piece.model.info;

import pl.illchess.domain.board.model.square.Square;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record PieceAttackingRay(Square occupiedSquare, Set<Square> attackingRay) {
    public Set<Square> fullAttackingRayWithOccupiedSquare() {
        return Stream.concat(Stream.of(occupiedSquare), attackingRay.stream()).collect(Collectors.toSet());
    }
}
