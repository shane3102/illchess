package pl.illchess.game.adapter.board.query.out.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.game.adapter.board.command.out.redis.model.GameEntity;
import pl.illchess.game.adapter.board.query.out.redis.mapper.BoardViewMapper;
import pl.illchess.game.application.game.query.out.GameViewPreMoveByUserQueryPort;
import pl.illchess.game.application.game.query.out.GameViewQueryPort;
import pl.illchess.game.application.game.query.out.model.GameView;
import pl.illchess.game.application.game.query.out.model.GameWithPreMovesView;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class GameViewRedisRepository implements GameViewQueryPort, GameViewPreMoveByUserQueryPort {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, GameEntity> template;

    @Override
    public Optional<GameView> findById(UUID boardId) {
        GameEntity readGameEntity = (GameEntity) template.opsForHash().get(BOARD_HASH_KEY, boardId.toString());

        GameView board = BoardViewMapper.toView(readGameEntity);

        return Optional.ofNullable(board);
    }

    @Override
    public Optional<GameWithPreMovesView> findByIdAndUsername(UUID boardId, String username) {
        GameEntity readGameEntity = (GameEntity) template.opsForHash().get(BOARD_HASH_KEY, boardId.toString());
        if (Objects.equals(readGameEntity == null ? null : readGameEntity.boardState().whitePlayer().username(), username)) {
            return Optional.ofNullable(getBoardPreMoveViewByPlayer(readGameEntity, readGameEntity.boardState().whitePlayer()));
        } else if (Objects.equals(readGameEntity == null ? null : readGameEntity.boardState().blackPlayer() == null ? null : readGameEntity.boardState().blackPlayer().username(), username)) {
            return Optional.ofNullable(getBoardPreMoveViewByPlayer(readGameEntity, readGameEntity.boardState().blackPlayer()));
        } else {
            return Optional.empty();
        }
    }

    private static GameWithPreMovesView getBoardPreMoveViewByPlayer(GameEntity readGameEntity, GameEntity.PlayerEntity player) {
        List<GameEntity.PreMoveEntity> userPreMoves = player.preMoves();
        if (userPreMoves.isEmpty()) {
            return null;
        }
        GameEntity.PreMoveEntity lastPreMove = userPreMoves.get(userPreMoves.size() - 1);
        List<GameEntity.PieceEntity> pieces = lastPreMove.piecesLocationsAfterPreMove();
        return BoardViewMapper.toViewAsPreMove(readGameEntity, userPreMoves, pieces);
    }
}
