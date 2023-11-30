package pl.illchess.adapter.board.query.out.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.adapter.board.query.out.redis.mapper.BoardViewMapper;
import pl.illchess.application.board.query.out.BoardViewQueryPort;
import pl.illchess.application.board.query.out.model.BoardView;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BoardViewRedisRepository implements BoardViewQueryPort {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, BoardEntity> template;

    @Override
    public Optional<BoardView> findById(UUID boardId) {
        BoardEntity readBoardEntity = (BoardEntity) template.opsForHash().get(boardId.toString(), BOARD_HASH_KEY);

        BoardView board = BoardViewMapper.toView(readBoardEntity);

        return Optional.ofNullable(board);
    }
}
