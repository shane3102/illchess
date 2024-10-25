package pl.illchess.game.adapter.board.query.out.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.game.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.game.adapter.board.query.out.redis.mapper.BoardViewMapper;
import pl.illchess.game.application.board.query.out.BoardAdditionalInfoViewQueryPort;
import pl.illchess.game.application.board.query.out.model.BoardAdditionalInfoView;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class BoardAdditionalInfoViewRedisRepository implements BoardAdditionalInfoViewQueryPort {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, BoardEntity> template;

    @Override
    public Optional<BoardAdditionalInfoView> findBoardById(UUID boardId) {
        BoardEntity boardEntity = (BoardEntity) template.opsForHash().get(BOARD_HASH_KEY, boardId.toString());
        BoardAdditionalInfoView resultView = BoardViewMapper.toAdditionalInfoView(boardEntity);
        return Optional.ofNullable(resultView);
    }
}
