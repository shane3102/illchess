package pl.illchess.adapter.board.command.out.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.adapter.board.command.out.redis.mapper.BoardMapper;
import pl.illchess.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.application.board.command.out.LoadBoard;
import pl.illchess.application.board.command.out.SaveBoard;
import pl.illchess.domain.board.model.Board;
import pl.illchess.domain.board.model.BoardId;

import java.util.Optional;

@Repository
public class BoardRedisRepository implements SaveBoard, LoadBoard {

    private static final String BOARD_HASH_KEY = "BOARD";

    private RedisTemplate<String, BoardEntity> template;

    @Override
    public Optional<Board> loadBoard(BoardId boardId) {

        BoardEntity readBoardEntity = (BoardEntity) template.opsForHash().get(boardId.uuid().toString(), BOARD_HASH_KEY);

        Board board = BoardMapper.toDomain(readBoardEntity);

        return Optional.ofNullable(board);
    }

    @Override
    public void saveBoard(Board savedBoard) {
        BoardEntity savedEntity = BoardMapper.toEntity(savedBoard);

        template.opsForHash().put(savedEntity.boardId().toString(), BOARD_HASH_KEY, savedEntity);
    }
}
