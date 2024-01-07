package pl.illchess.domain.board.model.square;

import pl.illchess.domain.piece.model.PieceBehaviour;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.type.King;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<SimpleSquare> getAllConnected() {
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

    private Set<SimpleSquare> getAllConnectedTillPieceEncounteredRememberVisited(
            PieceColor currentPieceColor,
            PiecesLocations locations,
            Set<SimpleSquare> visitedSquares,
            boolean skipKing
    ) {
        Set<SimpleSquare> newVisitedSquares = Stream.concat(visitedSquares.stream(), Stream.of(square)).collect(Collectors.toSet());

        Set<SimpleSquare> leftNodeSquares = getLeftNodeSquares(currentPieceColor, locations, newVisitedSquares, skipKing);
        Set<SimpleSquare> rightNodeSquares = getRightNodeSquares(currentPieceColor, locations, newVisitedSquares, skipKing);

        return Stream.of(
                        newVisitedSquares,
                        leftNodeSquares,
                        rightNodeSquares
                )
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<SimpleSquare> getLeftNodeSquares(
            PieceColor currentPieceColor,
            PiecesLocations locations,
            Set<SimpleSquare> newVisitedSquares,
            boolean skipKing
    ) {
        Set<SimpleSquare> leftNodeSquares;
        if (leftNode == null) {
            leftNodeSquares = null;
        } else if (!newVisitedSquares.contains(leftNode.square)) {
            Square nextSquareValue = Square.valueOf(leftNode.square.name());
            Optional<PieceBehaviour> pieceOnSquare = locations.getPieceOnSquare(nextSquareValue);
            if (pieceOnSquare.isPresent() && !(skipKing && pieceOnSquare.get() instanceof King)) {
                if (Objects.equals(pieceOnSquare.get().color(), currentPieceColor)) {
                    leftNodeSquares = null;
                } else {
                    leftNodeSquares = Set.of(leftNode.square);
                }
            } else {
                leftNodeSquares = leftNode.getAllConnectedTillPieceEncounteredRememberVisited(currentPieceColor, locations, newVisitedSquares, skipKing);
            }
        } else {
            leftNodeSquares = null;
        }
        return leftNodeSquares;
    }

    private Set<SimpleSquare> getRightNodeSquares(
            PieceColor currentPieceColor,
            PiecesLocations locations,
            Set<SimpleSquare> newVisitedSquares,
            boolean skipKing
    ) {
        Set<SimpleSquare> rightNodeSquares;
        if (rightNode == null) {
            rightNodeSquares = null;
        } else if (!newVisitedSquares.contains(rightNode.square)) {
            Square nextSquareValue = Square.valueOf(rightNode.square.name());
            Optional<PieceBehaviour> pieceOnSquare = locations.getPieceOnSquare(nextSquareValue);
            if (pieceOnSquare.isPresent() && (skipKing && pieceOnSquare.get() instanceof King)) {
                if (Objects.equals(pieceOnSquare.get().color(), currentPieceColor)) {
                    rightNodeSquares = null;
                } else {
                    rightNodeSquares = Set.of(rightNode.square);
                }
            } else {
                rightNodeSquares = rightNode.getAllConnectedTillPieceEncounteredRememberVisited(currentPieceColor, locations, newVisitedSquares, skipKing);
            }
        } else {
            rightNodeSquares = null;
        }
        return rightNodeSquares;
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

        if (leftNode == null && rightNode == null) {
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

}
