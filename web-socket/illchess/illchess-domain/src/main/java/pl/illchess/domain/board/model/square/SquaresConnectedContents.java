package pl.illchess.domain.board.model.square;

import pl.illchess.domain.piece.model.info.PieceColor;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        SimpleSquare simpleSquare = SimpleSquare.valueOf(square.name());

        if (root == null) {
            return Collections.emptySet();
        }

        SquaresBidirectionalLinkedList nodeBySquare = root.getNodeBySquare(simpleSquare);

        if (nodeBySquare == null) {
            return Collections.emptySet();
        }

        return nodeBySquare.getClosestNeighbours()
            .stream()
            .map(it -> Square.valueOf(it.name()))
            .collect(Collectors.toSet());
    }

    public Set<Square> getClosestNonOccupiedNeighbours(
        Square square,
        PiecesLocations piecesLocations
    ) {
        SimpleSquare simpleSquare = SimpleSquare.valueOf(square.name());

        if (root == null) {
            return Collections.emptySet();
        }

        SquaresBidirectionalLinkedList nodeBySquare = root.getNodeBySquare(simpleSquare);

        if (nodeBySquare == null) {
            return Collections.emptySet();
        }

        return nodeBySquare.getClosestNeighboursByOccupiedStatus(piecesLocations, false)
            .stream()
            .map(it -> Square.valueOf(it.name()))
            .collect(Collectors.toSet());
    }

    public Set<Square> getClosestOccupiedNeighbours(
        Square square,
        PiecesLocations piecesLocations
    ) {
        SimpleSquare simpleSquare = SimpleSquare.valueOf(square.name());

        if (root == null) {
            return Collections.emptySet();
        }

        SquaresBidirectionalLinkedList nodeBySquare = root.getNodeBySquare(simpleSquare);

        if (nodeBySquare == null) {
            return Collections.emptySet();
        }

        return nodeBySquare.getClosestNeighboursByOccupiedStatus(piecesLocations, true)
            .stream()
            .map(it -> Square.valueOf(it.name()))
            .collect(Collectors.toSet());
    }

    public Set<Square> getConnectedUntilPieceEncountered(
        Square checkedSquare,
        PieceColor currentPieceColor,
        PiecesLocations locations
    ) {
        if (root == null) {
            return Collections.emptySet();
        }
        SimpleSquare simpleSquare = SimpleSquare.valueOf(checkedSquare.name());
        SquaresBidirectionalLinkedList nodeBySquare = root.getNodeBySquare(simpleSquare);

        if (nodeBySquare == null) {
            return Collections.emptySet();
        }

        return nodeBySquare.getAllConnectedTillPieceEncountered(currentPieceColor, locations)
            .stream()
            .filter(it -> !Objects.equals(it.name(), checkedSquare.name()))
            .map(it -> Square.valueOf(it.name()))
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
        SimpleSquare possiblePinningSquare = SimpleSquare.valueOf(possiblePinningPieceSquare.name());
        SimpleSquare pinningCapableSquare = SimpleSquare.valueOf(pinningCapablePieceSquare.name());
        SimpleSquare kingSquare = SimpleSquare.valueOf(kingPieceSquare.name());
        if (!root.containsSquares(possiblePinningSquare, pinningCapableSquare, kingSquare)) {
            return Collections.emptySet();
        }

        SquaresBidirectionalLinkedList nodeBySquare = root.getNodeBySquare(possiblePinningSquare);
        if (nodeBySquare == null) {
            return Collections.emptySet();
        }

        return nodeBySquare.getPinningRayBySquare(
                pinningCapableSquare,
                kingSquare,
                currentPieceColor,
                locations
            )
            .stream()
            .map(it -> Square.valueOf(it.name()))
            .collect(Collectors.toSet());

    }

    public Set<Square> getAttackRayOnGivenSquare(
        Square checkRayPieceCapableSquare,
        Square givenPieceSquare,
        PieceColor currentPieceColor,
        PiecesLocations locations
    ) {
        if (root == null) {
            return Collections.emptySet();
        }

        SimpleSquare checkRayCapableSquare = SimpleSquare.valueOf(checkRayPieceCapableSquare.name());
        SimpleSquare givenSquare = SimpleSquare.valueOf(givenPieceSquare.name());

        SquaresBidirectionalLinkedList nodeBySquare = root.getNodeBySquare(checkRayCapableSquare);
        if (nodeBySquare == null) {
            return Collections.emptySet();
        }

        Set<SimpleSquare> attackRayOnGivenSquare = nodeBySquare.getAttackRayOnGivenSquare(givenSquare, currentPieceColor, locations);

        return attackRayOnGivenSquare
            .stream()
            .map(it -> Square.valueOf(it.name()))
            .filter(it -> !it.equals(checkRayPieceCapableSquare))
            .collect(Collectors.toSet());
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
