package pl.illchess.game.adapter.board.query.out.redis;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.game.adapter.board.command.out.redis.model.GameEntity;
import pl.illchess.game.adapter.board.query.out.redis.mapper.BoardViewMapper;
import pl.illchess.game.application.game.query.out.GameFinishedQueryPort;
import pl.illchess.game.application.game.query.out.model.GameFinishedView;

@Repository
@RequiredArgsConstructor
public class GameFinishedRedisRepository implements GameFinishedQueryPort {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, GameEntity> template;

    @Override
    public Optional<GameFinishedView> findById(UUID boardId) {
        GameEntity gameEntity = (GameEntity) template.opsForHash().get(BOARD_HASH_KEY, boardId.toString());
        GameFinishedView view = BoardViewMapper.toBoardWithFinishedGameView(gameEntity);
        return Optional.ofNullable(view);
    }
}
