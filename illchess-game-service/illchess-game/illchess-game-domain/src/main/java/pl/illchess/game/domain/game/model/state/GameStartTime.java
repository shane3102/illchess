package pl.illchess.game.domain.game.model.state;

import java.time.LocalDateTime;

public record GameStartTime(LocalDateTime value) {
    static GameStartTime current() {
        return new GameStartTime(LocalDateTime.now());
    }
}
