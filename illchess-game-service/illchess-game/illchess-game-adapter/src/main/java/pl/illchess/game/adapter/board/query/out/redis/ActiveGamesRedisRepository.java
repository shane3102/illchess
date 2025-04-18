package pl.illchess.game.adapter.board.query.out.redis;

import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.game.adapter.board.command.out.redis.model.GameEntity;
import pl.illchess.game.application.game.query.out.ActiveGamesQueryPort;
import pl.illchess.game.application.game.query.out.model.ActiveGamesView;

@Repository
@RequiredArgsConstructor
public class ActiveGamesRedisRepository implements ActiveGamesQueryPort {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, GameEntity> template;

    @Override
    public ActiveGamesView activeGames() {
        return new ActiveGamesView(
            template.opsForHash()
                .entries(BOARD_HASH_KEY)
                .values()
                .stream()
                .map(board -> (GameEntity) board)
                .filter(board -> board.boardState().blackPlayer() != null)
                .sorted(Comparator.comparing(b -> b.boardState().startTime()))
                .map(GameEntity::gameId)
                .toList()
        );
    }
}
