package pl.illchess.adapter.board.query.out.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.adapter.board.query.out.redis.mapper.BoardViewMapper;
import pl.illchess.application.board.query.out.BoardViewPreMoveByUserQueryPort;
import pl.illchess.application.board.query.out.BoardViewQueryPort;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.application.board.query.out.model.BoardWithPreMovesView;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BoardViewRedisRepository implements BoardViewQueryPort, BoardViewPreMoveByUserQueryPort {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, BoardEntity> template;

    @Override
    public Optional<BoardView> findById(UUID boardId) {
        BoardEntity readBoardEntity = (BoardEntity) template.opsForHash().get(BOARD_HASH_KEY, boardId.toString());

        BoardView board = BoardViewMapper.toView(readBoardEntity);

        return Optional.ofNullable(board);
    }

    @Override
    public Optional<BoardWithPreMovesView> findByIdAndUsername(UUID boardId, String username) {
        BoardEntity readBoardEntity = (BoardEntity) template.opsForHash().get(BOARD_HASH_KEY, boardId.toString());
        if (Objects.equals(readBoardEntity == null ? null : readBoardEntity.boardState().whitePlayer().username(), username)) {
            return Optional.ofNullable(getBoardPreMoveViewByPlayer(readBoardEntity, readBoardEntity.boardState().whitePlayer()));
        } else if (Objects.equals(readBoardEntity == null ? null : readBoardEntity.boardState().blackPlayer() == null ? null : readBoardEntity.boardState().blackPlayer().username(), username)) {
            return Optional.ofNullable(getBoardPreMoveViewByPlayer(readBoardEntity, readBoardEntity.boardState().blackPlayer()));
        } else {
            return Optional.empty();
        }
    }

    private static BoardWithPreMovesView getBoardPreMoveViewByPlayer(BoardEntity readBoardEntity, BoardEntity.PlayerEntity player) {
        List<BoardEntity.PreMoveEntity> userPreMoves = player.preMoves();
        if (userPreMoves.isEmpty()) {
            return null;
        }
        BoardEntity.PreMoveEntity lastPreMove = userPreMoves.get(userPreMoves.size() - 1);
        List<BoardEntity.PieceEntity> pieces = lastPreMove.piecesLocationsAfterPreMove();
        return BoardViewMapper.toViewAsPreMove(readBoardEntity, userPreMoves, pieces);
    }
}
