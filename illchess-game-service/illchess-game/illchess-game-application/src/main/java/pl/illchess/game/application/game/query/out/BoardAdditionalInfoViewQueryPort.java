package pl.illchess.game.application.game.query.out;

import pl.illchess.game.application.game.query.out.model.BoardAdditionalInfoView;

import java.util.Optional;
import java.util.UUID;

public interface BoardAdditionalInfoViewQueryPort {
    Optional<BoardAdditionalInfoView> findBoardById(UUID boardId);
}
