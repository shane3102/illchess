package pl.illchess.game.adapter.game.query.out.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.game.adapter.game.command.out.redis.model.GameEntity;
import pl.illchess.game.adapter.game.query.out.redis.mapper.GameViewMapper;
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
    public Optional<GameView> findById(UUID gameId) {
        GameEntity readGameEntity = (GameEntity) template.opsForHash().get(BOARD_HASH_KEY, gameId.toString());

        GameView board = GameViewMapper.toView(readGameEntity);

        return Optional.ofNullable(board);
    }

    @Override
    public Optional<GameWithPreMovesView> findByIdAndUsername(UUID gameId, String username) {
        GameEntity readGameEntity = (GameEntity) template.opsForHash().get(BOARD_HASH_KEY, gameId.toString());
        if (Objects.equals(readGameEntity == null ? null : readGameEntity.gameInfo().whitePlayer().username(), username)) {
            return Optional.ofNullable(getBoardPreMoveViewByPlayer(readGameEntity, readGameEntity.gameInfo().whitePlayer()));
        } else if (Objects.equals(readGameEntity == null ? null : readGameEntity.gameInfo().blackPlayer() == null ? null : readGameEntity.gameInfo().blackPlayer().username(), username)) {
            return Optional.ofNullable(getBoardPreMoveViewByPlayer(readGameEntity, readGameEntity.gameInfo().blackPlayer()));
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
        return GameViewMapper.toViewAsPreMove(readGameEntity, userPreMoves, pieces);
    }
}
