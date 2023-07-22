package pl.illchess.domain;

import java.io.Serializable;
import java.util.UUID;

public record ChessBoardId(UUID uuid) implements Serializable {
}
