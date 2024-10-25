package pl.illchess.game.adapter.board.query.out.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.game.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.game.application.board.query.out.ActiveBoardsQueryPort;
import pl.illchess.game.application.board.query.out.model.ActiveBoardsView;

@Repository
@RequiredArgsConstructor
public class ActiveBoardsRedisRepository implements ActiveBoardsQueryPort {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, BoardEntity> template;

    @Override
    public ActiveBoardsView activeBoards() {
        return new ActiveBoardsView(
            template.opsForHash()
                .entries(BOARD_HASH_KEY)
                .values()
                .stream()
                .map(board -> (BoardEntity) board)
                .map(BoardEntity::boardId)
                .toList()
        );
    }
}
