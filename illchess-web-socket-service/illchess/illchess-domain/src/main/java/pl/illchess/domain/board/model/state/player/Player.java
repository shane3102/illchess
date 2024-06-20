package pl.illchess.domain.board.model.state.player;

import pl.illchess.domain.board.command.ProposeDraw;
import pl.illchess.domain.board.command.ProposeTakingBackMove;
import pl.illchess.domain.board.exception.UserIsAlreadyProposingDrawException;
import pl.illchess.domain.board.exception.UserIsAlreadyProposingTakingBackMoveException;

import java.util.Objects;

public final class Player {
    private final Username username;
    private IsProposingDraw isProposingDraw;
    private IsProposingTakingBackMove isProposingTakingBackMove;

    public Player(Username username) {
        this.username = username;
        this.isProposingDraw = new IsProposingDraw(false);
        this.isProposingTakingBackMove = new IsProposingTakingBackMove(false);
    }

    public Player(Username username, IsProposingDraw isProposingDraw, IsProposingTakingBackMove isProposingTakingBackMove) {
        this.username = username;
        this.isProposingDraw = isProposingDraw;
        this.isProposingTakingBackMove = isProposingTakingBackMove;
    }

    public Username username() {
        return username;
    }

    public IsProposingDraw isProposingDraw() {
        return isProposingDraw;
    }

    public void proposeDraw(ProposeDraw command) {
        if (this.isProposingDraw.value()) {
            throw new UserIsAlreadyProposingDrawException(command.username());
        }
        this.isProposingDraw = new IsProposingDraw(true);
    }

    public void resetProposingDraw() {
        this.isProposingDraw = new IsProposingDraw(false);
    }

    public IsProposingTakingBackMove isProposingTakingBackMove() {
        return isProposingTakingBackMove;
    }

    public void proposeTakeBackMove(ProposeTakingBackMove command) {
        if (isProposingTakingBackMove.value()) {
            throw new UserIsAlreadyProposingTakingBackMoveException(command.username());
        }
        this.isProposingTakingBackMove = new IsProposingTakingBackMove(true);
    }

    public void resetProposingTakingBackMove() {
        this.isProposingTakingBackMove = new IsProposingTakingBackMove(false);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Player) obj;
        return Objects.equals(this.username, that.username) &&
            Objects.equals(this.isProposingDraw, that.isProposingDraw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, isProposingDraw);
    }

}
