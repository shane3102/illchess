package pl.illchess.game.adapter.board.query.out.redis;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.game.adapter.board.command.out.redis.model.GameEntity;
import pl.illchess.game.adapter.board.query.out.redis.mapper.BoardViewMapper;
import pl.illchess.game.application.game.query.out.BoardGameFinishedQueryPort;
import pl.illchess.game.application.game.query.out.model.BoardGameFinishedView;

@Repository
@RequiredArgsConstructor
public class BoardGameFinishedRedisRepository implements BoardGameFinishedQueryPort {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, GameEntity> template;

    @Override
    public Optional<BoardGameFinishedView> findById(UUID boardId) {
        GameEntity gameEntity = (GameEntity) template.opsForHash().get(BOARD_HASH_KEY, boardId.toString());
        BoardGameFinishedView view = BoardViewMapper.toBoardWithFinishedGameView(gameEntity);
        return Optional.ofNullable(view);
    }
}
