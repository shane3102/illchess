package pl.illchess.game.application.board.query.out.model;

import java.util.UUID;

public record BoardGameObtainedInfoView(
    UUID boardId,
    BoardGameObtainedStatus status
) {

    public enum BoardGameObtainedStatus {
        SUCCESS, ERROR
    }
}
