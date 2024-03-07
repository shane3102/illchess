package pl.illchess.domain.board.command;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;

public record CheckLegalMoves(
    BoardId boardId,
    Square square
) {
}
