package pl.illchess.game.domain.board.model.history;

import pl.illchess.game.domain.board.model.square.Square;
import pl.illchess.game.domain.board.model.square.info.SquareRank;
import pl.illchess.game.domain.piece.model.Piece;
import pl.illchess.game.domain.piece.model.type.Pawn;

import java.util.Stack;

import static pl.illchess.game.domain.board.model.square.Square.A1;
import static pl.illchess.game.domain.board.model.square.Square.A8;
import static pl.illchess.game.domain.board.model.square.Square.E1;
import static pl.illchess.game.domain.board.model.square.Square.E8;
import static pl.illchess.game.domain.board.model.square.Square.H1;
import static pl.illchess.game.domain.board.model.square.Square.H8;
import static pl.illchess.game.domain.piece.model.info.PieceColor.BLACK;

public record MoveHistory(Stack<Move> moveStack) {

    public MoveHistory() {
        this(new Stack<>());
    }

    public void addMoveToHistory(Move addedMove) {
        moveStack().push(addedMove);
    }

    // TODO optional (?)
    public Move peekLastMove() {
        if (moveStack.isEmpty()) {
            return null;
        } else {
            return moveStack.peek();
        }
    }

    public Move takeBackLastMove() {
        return moveStack.pop();
    }

    public boolean didPieceMadeMove(Piece piece) {
        return moveStack.stream()
            .anyMatch(
                move -> move.targetSquare().equals(piece.square()) && move.movedPiece().getClass().equals(piece.getClass())
            );
    }

    public String castlingAvailabilityFen() {
        boolean whiteKingNotMoved = moveStack.stream().noneMatch(move -> move.startSquare().equals(E1));

        boolean whiteRookANotMoved = moveStack.stream().noneMatch(move -> move.startSquare().equals(A1));
        boolean whiteRookHNotMoved = moveStack.stream().noneMatch(move -> move.startSquare().equals(H1));

        boolean blackKingNotMoved = moveStack.stream().noneMatch(move -> move.startSquare().equals(E8));

        boolean blackRookANotMoved = moveStack.stream().noneMatch(move -> move.startSquare().equals(A8));
        boolean blackRookHNotMoved = moveStack.stream().noneMatch(move -> move.startSquare().equals(H8));

        boolean whiteCastlePossible = (whiteKingNotMoved && whiteRookANotMoved) || (whiteKingNotMoved && whiteRookHNotMoved);
        boolean blackCastlePossible = (blackKingNotMoved && blackRookANotMoved) || (blackKingNotMoved && blackRookHNotMoved);

        if (!whiteCastlePossible && !blackCastlePossible) {
            return "-";
        }

        return "%s%s%s%s".formatted(
            whiteKingNotMoved && whiteRookHNotMoved ? "K" : "",
            whiteKingNotMoved && whiteRookANotMoved ? "Q" : "",
            blackKingNotMoved && blackRookHNotMoved ? "k" : "",
            blackKingNotMoved && blackRookANotMoved ? "q" : ""
        );
    }

    public String isEnPassantPossibleFen() {
        if (moveStack.empty()) {
            return "-";
        }

        Move lastMove = moveStack.peek();

        Square startSquare = lastMove.startSquare();
        Square targetSquare = lastMove.targetSquare();
        boolean doesMoveFulfillCondition = startSquare.getRank() == SquareRank._2 && targetSquare.getRank() == SquareRank._4 ||
            startSquare.getRank() == SquareRank._7 && targetSquare.getRank() == SquareRank._5;

        if (lastMove.movedPiece() instanceof Pawn && doesMoveFulfillCondition) {
            return Square.fromRankAndFile(
                    startSquare.getFile().name().charAt(0),
                    targetSquare.getRank().getNumber() == 4 ? 3 : 6
                )
                .name()
                .toLowerCase();
        } else {
            return "-";
        }
    }

    public String halfMoveClockFen() {
        int result = 0;
        for (Move move : moveStack) {
            if (move.movedPiece() instanceof Pawn || move.capturedPiece() != null) {
                result = 0;
            } else if (move.movedPiece().color() == BLACK) {
                result += 1;

            }
        }
        return String.valueOf(result);
    }

    public String fullMoveCountFen() {
        int result = 0;
        for (Move move : moveStack) {
            if (move.movedPiece().color() == BLACK) {
                result += 1;
            }
        }
        return String.valueOf(result);
    }
}
