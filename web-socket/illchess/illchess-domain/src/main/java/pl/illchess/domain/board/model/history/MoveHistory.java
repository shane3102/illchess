package pl.illchess.domain.board.model.history;

import java.util.Stack;

public record MoveHistory(Stack<Move> moveStack) {

    public MoveHistory() {
        this(new Stack<>());
    }

    public void addMoveToHistory(Move addedMove) {
        moveStack().add(addedMove);
    }

    public Move peekLastMove() {
        return moveStack.peek();
    }

    public Move takeBackLastMove() {
        return moveStack.pop();
    }
}
