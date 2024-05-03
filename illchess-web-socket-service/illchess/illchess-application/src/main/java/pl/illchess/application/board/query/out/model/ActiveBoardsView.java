package pl.illchess.application.board.query.out.model;

import java.util.List;
import java.util.UUID;

public record ActiveBoardsView(List<UUID> activeBoardsIds) {
}
