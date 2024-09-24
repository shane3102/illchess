package pl.illchess.adapter.board.query.out.redis;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.adapter.board.query.out.redis.mapper.BoardViewMapper;
import pl.illchess.application.board.query.out.BoardGameFinishedQueryPort;
import pl.illchess.application.board.query.out.model.BoardGameFinishedView;

@Repository
@RequiredArgsConstructor
public class BoardGameFinishedRedisRepository implements BoardGameFinishedQueryPort {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, BoardEntity> template;

    @Override
    public Optional<BoardGameFinishedView> findById(UUID boardId) {
        BoardEntity boardEntity = (BoardEntity) template.opsForHash().get(BOARD_HASH_KEY, boardId.toString());
        BoardGameFinishedView view = BoardViewMapper.toBoardWithFinishedGameView(boardEntity);
        return Optional.ofNullable(view);
    }
}
