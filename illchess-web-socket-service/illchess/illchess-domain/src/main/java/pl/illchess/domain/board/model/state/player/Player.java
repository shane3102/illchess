package pl.illchess.domain.board.model.state.player;

import pl.illchess.domain.board.command.ProposeDraw;
import pl.illchess.domain.board.exception.UserIsAlreadyProposingDrawException;

import java.util.Objects;

public final class Player {
    private final Username username;
    private IsProposingDraw isProposingDraw;

    public Player(Username username, IsProposingDraw isProposingDraw) {
        this.username = username;
        this.isProposingDraw = isProposingDraw;
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

    @Override
    public String toString() {
        return "Player[" +
            "username=" + username + ", " +
            "isProposingDraw=" + isProposingDraw + ']';
    }

}
