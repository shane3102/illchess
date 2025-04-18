package pl.illchess.game.adapter.game.query.out.redis;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.game.adapter.game.command.out.redis.model.GameEntity;
import pl.illchess.game.adapter.game.query.out.redis.mapper.GameViewMapper;
import pl.illchess.game.application.game.query.out.GameFinishedQueryPort;
import pl.illchess.game.application.game.query.out.model.GameFinishedView;

@Repository
@RequiredArgsConstructor
public class GameFinishedRedisRepository implements GameFinishedQueryPort {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, GameEntity> template;

    @Override
    public Optional<GameFinishedView> findById(UUID gameId) {
        GameEntity gameEntity = (GameEntity) template.opsForHash().get(BOARD_HASH_KEY, gameId.toString());
        GameFinishedView view = GameViewMapper.toBoardWithFinishedGameView(gameEntity);
        return Optional.ofNullable(view);
    }
}
