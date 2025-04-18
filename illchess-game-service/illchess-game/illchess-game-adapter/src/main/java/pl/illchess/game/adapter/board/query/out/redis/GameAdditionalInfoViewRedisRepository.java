package pl.illchess.game.adapter.board.query.out.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.game.adapter.board.command.out.redis.model.GameEntity;
import pl.illchess.game.adapter.board.query.out.redis.mapper.BoardViewMapper;
import pl.illchess.game.application.game.query.out.GameAdditionalInfoViewQueryPort;
import pl.illchess.game.application.game.query.out.model.GameAdditionalInfoView;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class GameAdditionalInfoViewRedisRepository implements GameAdditionalInfoViewQueryPort {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, GameEntity> template;

    @Override
    public Optional<GameAdditionalInfoView> findGameById(UUID boardId) {
        GameEntity gameEntity = (GameEntity) template.opsForHash().get(BOARD_HASH_KEY, boardId.toString());
        GameAdditionalInfoView resultView = BoardViewMapper.toAdditionalInfoView(gameEntity);
        return Optional.ofNullable(resultView);
    }
}
