package pl.illchess.domain.board.model.history;

import java.util.Stack;

// TODO seraizliation of this  class  if fucked, taking back last move not possible
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
}
