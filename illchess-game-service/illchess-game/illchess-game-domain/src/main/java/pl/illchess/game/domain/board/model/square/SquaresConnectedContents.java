package pl.illchess.game.domain.board.model.square;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import pl.illchess.game.domain.piece.model.info.PieceAttackingRay.SquaresInRay;
import pl.illchess.game.domain.piece.model.info.PieceColor;

// TODO operate on streams instead of collecting everything every five fucking minutes
public class SquaresConnectedContents {

    private final SquaresBidirectionalLinkedList root;

    private SquaresConnectedContents(SquaresBidirectionalLinkedList root) {
        this.root = root;
    }

    public SquaresConnectedContents() {
        this.root = null;
    }

    public Set<Square> getClosestNeighbours(
        Square square
    ) {
        if (root == null) {
            return Collections.emptySet();
        }

        SquaresBidirectionalLinkedList nodeBySquare = root.getNodeBySquare(square.toSimple());

        if (nodeBySquare == null) {
            return Collections.emptySet();
        }

        return nodeBySquare.getClosestNeighbours();
    }

    public Set<Square> getClosestNonOccupiedNeighbours(
        Square square,
        PiecesLocations piecesLocations
    ) {
        if (root == null) {
            return Collections.emptySet();
        }

        SquaresBidirectionalLinkedList nodeBySquare = root.getNodeBySquare(square.toSimple());

        if (nodeBySquare == null) {
            return Collections.emptySet();
        }

        return nodeBySquare.getClosestNeighboursByOccupiedStatus(piecesLocations, false);
    }

    public Set<Square> getClosestOccupiedNeighbours(
        Square square,
        PiecesLocations piecesLocations
    ) {
        if (root == null) {
            return Collections.emptySet();
        }

        SquaresBidirectionalLinkedList nodeBySquare = root.getNodeBySquare(square.toSimple());

        if (nodeBySquare == null) {
            return Collections.emptySet();
        }

        return nodeBySquare.getClosestNeighboursByOccupiedStatus(piecesLocations, true);
    }

    public Set<Square> getConnectedUntilPieceEncountered(
        Square checkedSquare,
        PieceColor currentPieceColor,
        PiecesLocations locations
    ) {
        if (root == null) {
            return Collections.emptySet();
        }
        SquaresBidirectionalLinkedList nodeBySquare = root.getNodeBySquare(checkedSquare.toSimple());

        if (nodeBySquare == null) {
            return Collections.emptySet();
        }

        return nodeBySquare.getAllConnectedTillPieceEncountered(currentPieceColor, locations)
            .stream()
            .filter(it -> !Objects.equals(it.name(), checkedSquare.name()))
            .collect(Collectors.toSet());
    }

    public Set<Square> getPinningRayBySquare(
        Square possiblePinningPieceSquare,
        Square pinningCapablePieceSquare,
        Square kingPieceSquare,
        PieceColor currentPieceColor,
        PiecesLocations locations
    ) {
        if (root == null) {
            return Collections.emptySet();
        }
        if (!root.containsSquares(possiblePinningPieceSquare, pinningCapablePieceSquare, kingPieceSquare)) {
            return Collections.emptySet();
        }

        SquaresBidirectionalLinkedList nodeBySquare = root.getNodeBySquare(possiblePinningPieceSquare.toSimple());
        if (nodeBySquare == null) {
            return Collections.emptySet();
        }

        return nodeBySquare.getPinningRayBySquare(
            pinningCapablePieceSquare,
            kingPieceSquare,
            currentPieceColor,
            locations
        );

    }

    public Set<Square> getFullRay() {
        if (root == null) {
            return Collections.emptySet();
        }
        return root.getAllConnected();
    }

    public SquaresInRay getAttackRayOnGivenSquare(
        Square checkRayPieceCapableSquare,
        Square givenPieceSquare,
        PieceColor currentPieceColor,
        PiecesLocations locations
    ) {
        if (root == null) {
            return SquaresInRay.empty();
        }

        SquaresBidirectionalLinkedList nodeBySquare = root.getNodeBySquare(checkRayPieceCapableSquare.toSimple());
        if (nodeBySquare == null) {
            return SquaresInRay.empty();
        }
        Set<Square> fullRay = nodeBySquare.getAllConnected();
        if (!fullRay.contains(givenPieceSquare)) {
            return SquaresInRay.empty();
        }

        Set<Square> rayUntilPieceEncountered = nodeBySquare.getAttackingRayOnGivenSquare(givenPieceSquare, currentPieceColor, locations);

        Set<Square> attackRayOnGivenSquare = nodeBySquare.getFullAttackRayOnGivenSquare(givenPieceSquare, currentPieceColor, locations);

        Set<Square> attackingRay = attackRayOnGivenSquare
            .stream()
            .filter(it -> !it.equals(checkRayPieceCapableSquare))
            .collect(Collectors.toSet());

        return new SquaresInRay(attackingRay, rayUntilPieceEncountered);
    }

    public static SquaresConnectedContents of(SimpleSquare... squares) {

        if (squares.length == 0) {
            return new SquaresConnectedContents();
        }
        SquaresBidirectionalLinkedList result = new SquaresBidirectionalLinkedList(squares[0]);

        for (int i = 1; i < squares.length; i++) {
            SimpleSquare previousSquare = squares[i - 1];
            SimpleSquare currentSquare = squares[i];

            SquaresBidirectionalLinkedList previousNode = result.getNodeBySquare(previousSquare);
            SquaresBidirectionalLinkedList currentNode = new SquaresBidirectionalLinkedList(currentSquare);

            currentNode.setLeftNode(previousNode);
            previousNode.setRightNode(currentNode);

        }

        return new SquaresConnectedContents(result);
    }

}
