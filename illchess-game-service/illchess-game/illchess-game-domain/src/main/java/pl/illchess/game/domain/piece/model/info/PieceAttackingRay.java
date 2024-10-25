package pl.illchess.game.domain.piece.model.info;

import pl.illchess.game.domain.board.model.square.Square;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record PieceAttackingRay(Square occupiedSquare, SquaresInRay squaresInRay) {

    public PieceAttackingRay(Square occupiedSquare, Set<Square> squaresInRay) {
        this(occupiedSquare, new SquaresInRay(squaresInRay, squaresInRay));
    }

    public Set<Square> fullAttackingRayWithOccupiedSquare() {
        return Stream.concat(Stream.of(occupiedSquare), squaresInRay.rayUntilPiece().stream()).collect(Collectors.toSet());
    }

    public Set<Square> rayUntilPieceEncounteredWithoutOccupiedSquare() {
        return squaresInRay.attackingRay().stream().filter(square -> !square.equals(occupiedSquare)).collect(Collectors.toSet());
    }

    public record SquaresInRay(Set<Square> attackingRay, Set<Square> rayUntilPiece) {

        public boolean isAttackingRayEmpty() {
            return attackingRay == null || attackingRay.isEmpty();
        }

        public boolean isEmpty() {
            return (attackingRay == null || attackingRay.isEmpty()) && (rayUntilPiece == null || rayUntilPiece.isEmpty());
        }

        public static SquaresInRay empty() {
            return new SquaresInRay(Set.of(), Set.of());
        }
    }
}
