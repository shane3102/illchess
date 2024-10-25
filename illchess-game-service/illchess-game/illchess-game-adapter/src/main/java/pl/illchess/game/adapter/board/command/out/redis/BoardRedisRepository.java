package pl.illchess.game.adapter.board.command.out.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.game.adapter.board.command.out.redis.mapper.BoardMapper;
import pl.illchess.game.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.game.application.board.command.out.DeleteBoard;
import pl.illchess.game.application.board.command.out.LoadBoard;
import pl.illchess.game.application.board.command.out.SaveBoard;
import pl.illchess.game.domain.board.model.Board;
import pl.illchess.game.domain.board.model.BoardId;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRedisRepository implements SaveBoard, LoadBoard, DeleteBoard {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, BoardEntity> template;

    @Override
    public Optional<Board> loadBoard(BoardId boardId) {

        BoardEntity readBoardEntity = (BoardEntity) template.opsForHash().get(BOARD_HASH_KEY, boardId.uuid().toString());

        Board board = BoardMapper.toDomain(readBoardEntity);

        return Optional.ofNullable(board);
    }

    @Override
    public Optional<Board> loadBoardWithoutPlayer() {
        return template.opsForHash()
            .entries(BOARD_HASH_KEY)
            .values()
            .stream()
            .map(board -> (BoardEntity) board)
            .map(BoardMapper::toDomain)
            .filter(board -> board.boardState().blackPlayer() == null)
            .findFirst();
    }

    @Override
    public BoardId saveBoard(Board savedBoard) {
        BoardEntity savedEntity = BoardMapper.toEntity(savedBoard);
        template.opsForHash().put(BOARD_HASH_KEY, savedEntity.boardId().toString(), savedEntity);
        return savedBoard.boardId();
    }

    @Override
    public void deleteBoard(BoardId boardId) {
        template.opsForHash().delete(BOARD_HASH_KEY, boardId.uuid().toString());
    }
}
