package pl.illchess.game.domain.board.model.square;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import pl.illchess.game.domain.piece.model.Piece;
import pl.illchess.game.domain.piece.model.info.PieceColor;
import pl.illchess.game.domain.piece.model.type.King;

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

    public Set<Square> getClosestNeighbours() {
        return Stream.of(
                leftNode == null ? null : Square.valueOf(leftNode.square.name()),
                rightNode == null ? null : Square.valueOf(rightNode.square.name())
            )
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    public Set<Square> getClosestNeighboursByOccupiedStatus(
        PiecesLocations piecesLocations,
        boolean occupiedStatus
    ) {
        Set<Square> result = new HashSet<>();

        boolean doGetSquareOnLeftNode = doGetSquareByOccupiedStatus(leftNode, piecesLocations, occupiedStatus);
        boolean doGetSquareOnRightNode = doGetSquareByOccupiedStatus(rightNode, piecesLocations, occupiedStatus);

        if (doGetSquareOnLeftNode) {
            result.add(Square.valueOf(leftNode.square.name()));
        }
        if (doGetSquareOnRightNode) {
            result.add(Square.valueOf(rightNode.square.name()));
        }

        return result;
    }

    SquaresBidirectionalLinkedList getNodeBySquare(SimpleSquare square) {
        return getNodeBySquareWithAndRememberVisited(square, Set.of());
    }

    public Set<Square> getAllConnectedXRayingKing(
        PieceColor currentPieceColor,
        PiecesLocations locations
    ) {
        return getAllConnectedTillPieceEncounteredRememberVisited(
            currentPieceColor,
            locations,
            Set.of(Square.valueOf(square.name())),
            true
        );
    }

    public Set<Square> getAllConnectedTillPieceEncountered(
        PieceColor currentPieceColor,
        PiecesLocations locations
    ) {
        return getAllConnectedTillPieceEncounteredRememberVisited(
            currentPieceColor,
            locations,
            Set.of(Square.valueOf(square.name())),
            false
        );
    }

    public Set<Square> getPinningRayBySquare(
        Square pinningCapableSquare,
        Square kingSquare,
        PieceColor currentPieceColor,
        PiecesLocations piecesLocations
    ) {
        Set<Square> pinningRay = getAllConnectedXRayingKing(currentPieceColor, piecesLocations);

        if (!pinningRay.contains(pinningCapableSquare) || !pinningRay.contains(kingSquare)) {
            return Collections.emptySet();
        }

        return pinningRay;
    }

    public Set<Square> getFullAttackRayOnGivenSquare(
        Square givenSquare,
        PieceColor currentPieceColor,
        PiecesLocations locations
    ) {
        return getConnectedBySkipKingRememberVisited(
            givenSquare,
            currentPieceColor,
            locations,
            Set.of(Square.valueOf(square.name())),
            true
        );
    }

    public Set<Square> getAttackingRayOnGivenSquare(
        Square givenSquare,
        PieceColor currentPieceColor,
        PiecesLocations locations
    ) {
        return getConnectedBySkipKingRememberVisited(
            givenSquare,
            currentPieceColor,
            locations,
            Set.of(Square.valueOf(square.name())),
            false
        );
    }

    private Set<Square> getConnectedBySkipKingRememberVisited(
        Square givenSquare,
        PieceColor currentPieceColor,
        PiecesLocations locations,
        Set<Square> visitedSquares,
        boolean skipKing
    ) {
        Set<Square> newVisitedSquares = Stream.concat(
                visitedSquares.stream(),
                Stream.of(Square.valueOf(square.name()))
            )
            .collect(Collectors.toSet());

        Set<Square> leftNodeSquares = getNodeSquares(leftNode, currentPieceColor, locations, newVisitedSquares, skipKing);
        Set<Square> rightNodeSquares = getNodeSquares(rightNode, currentPieceColor, locations, newVisitedSquares, skipKing);

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

    private Set<Square> getAllConnectedTillPieceEncounteredRememberVisited(
        PieceColor currentPieceColor,
        PiecesLocations locations,
        Set<Square> visitedSquares,
        boolean skipKing
    ) {
        Set<Square> newVisitedSquares = Stream.concat(visitedSquares.stream(), Stream.of(Square.valueOf(square.name()))).collect(Collectors.toSet());

        Set<Square> leftNodeSquares = getNodeSquares(leftNode, currentPieceColor, locations, newVisitedSquares, skipKing);
        Set<Square> rightNodeSquares = getNodeSquares(rightNode, currentPieceColor, locations, newVisitedSquares, skipKing);

        return Stream.of(
                newVisitedSquares,
                leftNodeSquares,
                rightNodeSquares
            )
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    private Set<Square> getNodeSquares(
        SquaresBidirectionalLinkedList examinedNode,
        PieceColor currentPieceColor,
        PiecesLocations locations,
        Set<Square> newVisitedSquares,
        boolean skipKing
    ) {
        Set<Square> nodeSquares;
        if (examinedNode == null) {
            nodeSquares = null;
        } else if (!newVisitedSquares.contains(Square.valueOf(examinedNode.square.name()))) {
            Square nextSquareValue = Square.valueOf(examinedNode.square.name());
            Optional<Piece> pieceOnSquare = locations.findPieceOnSquare(nextSquareValue);
            if (pieceOnSquare.isPresent() && !(skipKing && pieceOnSquare.get() instanceof King && pieceOnSquare.get().color() != currentPieceColor)) {
                nodeSquares = Set.of(Square.valueOf(examinedNode.square.name()));
            } else {
                nodeSquares = examinedNode.getAllConnectedTillPieceEncounteredRememberVisited(currentPieceColor, locations, newVisitedSquares, skipKing);
            }
        } else {
            nodeSquares = null;
        }
        return nodeSquares;
    }

    private SquaresBidirectionalLinkedList getNodeBySquareWithAndRememberVisited(
        SimpleSquare searchedSquareNode,
        Set<SimpleSquare> visitedSquares
    ) {
        if (Objects.equals(searchedSquareNode, this.square)) {
            return this;
        }

        if (visitedSquares.contains(searchedSquareNode)) {
            return null;
        }

        Set<SimpleSquare> updatedSet = Stream.concat(visitedSquares.stream(), Stream.of(this.square))
            .collect(Collectors.toSet());

        SquaresBidirectionalLinkedList leftNodeResult = leftNode == null || updatedSet.contains(leftNode.square)
            ? null
            : leftNode.getNodeBySquareWithAndRememberVisited(searchedSquareNode, updatedSet);
        SquaresBidirectionalLinkedList rightNodeResult = rightNode == null || updatedSet.contains(rightNode.square)
            ? null
            : rightNode.getNodeBySquareWithAndRememberVisited(searchedSquareNode, updatedSet);
        return rightNodeResult != null ? rightNodeResult : leftNodeResult;
    }

    public boolean containsSquares(Square... squares) {
        return getAllConnected().containsAll(List.of(squares));
    }

    public Set<Square> getAllConnected() {
        return getAllConnectedRememberVisited(List.of());
    }

    private Set<Square> getAllConnectedRememberVisited(
        List<Square> visited
    ) {

        List<Square> newVisited = Stream.concat(visited.stream(), Stream.of(Square.valueOf(square.name()))).toList();

        Set<Square> leftNodeDependends = leftNode == null || visited.contains(Square.valueOf(leftNode.square.name()))
            ? Set.of()
            : leftNode.getAllConnectedRememberVisited(newVisited);

        Set<Square> rightNodeDependends = rightNode == null || visited.contains(Square.valueOf(rightNode.square.name()))
            ? Set.of()
            : rightNode.getAllConnectedRememberVisited(newVisited);

        return Stream.concat(
                newVisited.stream(),
                Stream.concat(rightNodeDependends.stream(), leftNodeDependends.stream())
            )
            .collect(Collectors.toSet());

    }

    private boolean doGetSquareByOccupiedStatus(
        SquaresBidirectionalLinkedList node,
        PiecesLocations piecesLocations,
        boolean occupiedStatus
    ) {
        if (node == null) {
            return false;
        }
        Optional<Piece> allegedPieceOnSquare = piecesLocations.findPieceOnSquare(Square.valueOf(node.square.name()));

        return occupiedStatus
            ? allegedPieceOnSquare.isPresent()
            : allegedPieceOnSquare.isEmpty();
    }

}
