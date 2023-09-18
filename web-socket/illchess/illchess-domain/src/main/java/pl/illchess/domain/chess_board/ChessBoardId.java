package pl.illchess.domain.chess_board;

import java.io.Serializable;
import java.util.UUID;

public record ChessBoardId(UUID uuid) implements Serializable {
}
