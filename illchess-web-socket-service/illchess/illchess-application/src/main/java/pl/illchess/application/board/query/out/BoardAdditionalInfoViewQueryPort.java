package pl.illchess.application.board.query.out;

import pl.illchess.application.board.query.out.model.BoardAdditionalInfoView;

import java.util.Optional;
import java.util.UUID;

public interface BoardAdditionalInfoViewQueryPort {
    Optional<BoardAdditionalInfoView> findBoardById(UUID boardId);
}
