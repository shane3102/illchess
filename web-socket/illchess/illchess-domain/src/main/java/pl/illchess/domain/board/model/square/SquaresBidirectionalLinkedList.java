package pl.illchess.domain.board.model.square;

import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.type.King;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO operate on streams instead of collecting everything every five fucking minutes
public final class SquaresBidirectionalLinkedList {
    private final SimpleSquare square;
    private SquaresBidirectionalLinkedList leftNode;
    private SquaresBidirectionalLinkedList rightNode;

    public SquaresBidirectionalLinkedList(SimpleSquare square) {
        this.square = square;
    }

    public void setLeftNode(SquaresBidirectionalLinkedList leftNode) {
        this.leftNode = leftNode;
    }

    public void setRightNode(SquaresBidirectionalLinkedList rightNode) {
        this.rightNode = rightNode;
    }

    public List<SimpleSquare> getClosestNeighbours() {
        return Stream.of(
                        leftNode == null ? null : leftNode.square,
                        rightNode == null ? null : rightNode.square
                )
                .filter(Objects::nonNull)
                .toList();
    }

    public Set<SimpleSquare> getClosestNeighboursByOccupiedStatus(
            PiecesLocations piecesLocations,
            boolean occupiedStatus
    ) {
        return Stream.of(
                        leftNode == null
                                ||
                                (occupiedStatus
                                        ? piecesLocations.findPieceOnSquare(Square.valueOf(leftNode.square.name())).isEmpty()
                                        : piecesLocations.findPieceOnSquare(Square.valueOf(leftNode.square.name())).isPresent()
                                )
                                ? null :
                                leftNode.square,
                        rightNode == null
                                || (occupiedStatus
                                ? piecesLocations.findPieceOnSquare(Square.valueOf(rightNode.square.name())).isEmpty()
                                : piecesLocations.findPieceOnSquare(Square.valueOf(rightNode.square.name())).isPresent()
                        )
                                ? null :
                                rightNode.square
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    SquaresBidirectionalLinkedList getNodeBySquare(SimpleSquare square) {
        return getNodeBySquareWithAndRememberVisited(square, Set.of());
    }

    public Set<SimpleSquare> getAllConnectedXRayingKing(
            PieceColor currentPieceColor,
            PiecesLocations locations
    ) {
        return getAllConnectedTillPieceEncounteredRememberVisited(currentPieceColor, locations, Set.of(square), true);
    }

    public Set<SimpleSquare> getAllConnectedTillPieceEncountered(
            PieceColor currentPieceColor,
            PiecesLocations locations
    ) {
        return getAllConnectedTillPieceEncounteredRememberVisited(currentPieceColor, locations, Set.of(square), false);
    }

    public Set<SimpleSquare> getPinningRayBySquare(
            SimpleSquare pinningCapableSquare,
            SimpleSquare kingSquare,
            PieceColor currentPieceColor,
            PiecesLocations piecesLocations
    ) {
        Set<SimpleSquare> pinningRay = getAllConnectedXRayingKing(currentPieceColor, piecesLocations);

        if (!pinningRay.contains(pinningCapableSquare) || !pinningRay.contains(kingSquare)) {
            return Collections.emptySet();
        }

        return pinningRay;
    }

    public Set<SimpleSquare> getAttackRayOnGivenSquare(
            SimpleSquare givenSquare,
            PieceColor currentPieceColor,
            PiecesLocations locations
    ) {
        return getConnectedContainingKingSquareRememberVisited(givenSquare, currentPieceColor, locations, Set.of(square));
    }

    private Set<SimpleSquare> getConnectedContainingKingSquareRememberVisited(
            SimpleSquare givenSquare,
            PieceColor currentPieceColor,
            PiecesLocations locations,
            Set<SimpleSquare> visitedSquares
    ) {
        Set<SimpleSquare> newVisitedSquares = Stream.concat(visitedSquares.stream(), Stream.of(square)).collect(Collectors.toSet());

        Set<SimpleSquare> leftNodeSquares = getNodeSquares(leftNode, currentPieceColor, locations, newVisitedSquares, false);
        Set<SimpleSquare> rightNodeSquares = getNodeSquares(rightNode, currentPieceColor, locations, newVisitedSquares, false);

        if (leftNodeSquares != null && leftNodeSquares.contains(givenSquare)) {
            return Stream.of(
                            newVisitedSquares,
                            leftNodeSquares
                    )
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        } else if (rightNodeSquares != null && rightNodeSquares.contains(givenSquare)) {
            return Stream.of(
                            newVisitedSquares,
                            rightNodeSquares
                    )
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }


    private Set<SimpleSquare> getAllConnectedTillPieceEncounteredRememberVisited(
            PieceColor currentPieceColor,
            PiecesLocations locations,
            Set<SimpleSquare> visitedSquares,
            boolean skipKing
    ) {
        Set<SimpleSquare> newVisitedSquares = Stream.concat(visitedSquares.stream(), Stream.of(square)).collect(Collectors.toSet());

        Set<SimpleSquare> leftNodeSquares = getNodeSquares(leftNode, currentPieceColor, locations, newVisitedSquares, skipKing);
        Set<SimpleSquare> rightNodeSquares = getNodeSquares(rightNode, currentPieceColor, locations, newVisitedSquares, skipKing);

        return Stream.of(
                        newVisitedSquares,
                        leftNodeSquares,
                        rightNodeSquares
                )
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<SimpleSquare> getNodeSquares(
            SquaresBidirectionalLinkedList examinedNode,
            PieceColor currentPieceColor,
            PiecesLocations locations,
            Set<SimpleSquare> newVisitedSquares,
            boolean skipKing
    ) {
        Set<SimpleSquare> nodeSquares;
        if (examinedNode == null) {
            nodeSquares = null;
        } else if (!newVisitedSquares.contains(examinedNode.square)) {
            Square nextSquareValue = Square.valueOf(examinedNode.square.name());
            Optional<Piece> pieceOnSquare = locations.findPieceOnSquare(nextSquareValue);
            if (pieceOnSquare.isPresent() && (!skipKing || !(pieceOnSquare.get() instanceof King))) {
                nodeSquares = Set.of(examinedNode.square);
            } else {
                nodeSquares = examinedNode.getAllConnectedTillPieceEncounteredRememberVisited(currentPieceColor, locations, newVisitedSquares, skipKing);
            }
        } else {
            nodeSquares = null;
        }
        return nodeSquares;
    }

    private SquaresBidirectionalLinkedList getNodeBySquareWithAndRememberVisited(
            SimpleSquare square,
            Set<SimpleSquare> visitedSquares
    ) {
        if (Objects.equals(square, this.square)) {
            return this;
        }

        if (visitedSquares.contains(square)) {
            return null;
        }

        Set<SimpleSquare> updatedSet = Stream.concat(visitedSquares.stream(), Stream.of(this.square))
                .collect(Collectors.toSet());

        SquaresBidirectionalLinkedList leftNodeResult = leftNode == null || updatedSet.contains(leftNode.square)
                ? null
                : leftNode.getNodeBySquareWithAndRememberVisited(square, updatedSet);
        SquaresBidirectionalLinkedList rightNodeResult = rightNode == null || updatedSet.contains(rightNode.square)
                ? null
                : rightNode.getNodeBySquareWithAndRememberVisited(square, updatedSet);
        return rightNodeResult != null ? rightNodeResult : leftNodeResult;
    }

    public boolean containsSquares(SimpleSquare... squares) {
        return getAllConnected().containsAll(List.of(squares));
    }

    private List<SimpleSquare> getAllConnected() {
        return getAllConnectedRememberVisited(List.of());
    }

    private List<SimpleSquare> getAllConnectedRememberVisited(
            List<SimpleSquare> visited
    ) {

        List<SimpleSquare> newVisited = Stream.concat(visited.stream(), Stream.of(square)).toList();

        List<SimpleSquare> leftNodeDependends = leftNode == null || visited.contains(leftNode.square)
                ? List.of()
                : leftNode.getAllConnectedRememberVisited(newVisited);

        List<SimpleSquare> rightNodeDependends = rightNode == null || visited.contains(rightNode.square)
                ? List.of()
                : rightNode.getAllConnectedRememberVisited(newVisited);

        return Stream.concat(
                        newVisited.stream(),
                        Stream.concat(rightNodeDependends.stream(), leftNodeDependends.stream())
                )
                .toList();

    }

}
