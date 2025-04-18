package pl.illchess.game.application.game.query.out.model;

import java.util.UUID;

public record GameObtainedInfoView(
    UUID gameId,
    GameObtainedStatus status
) {

    public enum GameObtainedStatus {
        SUCCESS, ERROR
    }
}
