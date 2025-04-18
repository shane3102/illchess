package pl.illchess.game.adapter.board.command.out.redis;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.game.adapter.board.command.out.redis.mapper.BoardMapper;
import pl.illchess.game.adapter.board.command.out.redis.model.GameEntity;
import pl.illchess.game.application.board.command.out.DeleteBoard;
import pl.illchess.game.application.board.command.out.LoadBoard;
import pl.illchess.game.application.board.command.out.SaveBoard;
import pl.illchess.game.domain.game.model.Game;
import pl.illchess.game.domain.game.model.GameId;

import java.util.Optional;
import pl.illchess.game.domain.game.model.state.player.Username;

@Repository
@RequiredArgsConstructor
public class BoardRedisRepository implements SaveBoard, LoadBoard, DeleteBoard {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, GameEntity> template;

    @Override
    public Optional<Game> loadBoard(GameId gameId) {

        GameEntity readGameEntity = (GameEntity) template.opsForHash().get(BOARD_HASH_KEY, gameId.uuid().toString());

        Game game = BoardMapper.toDomain(readGameEntity);

        return Optional.ofNullable(game);
    }

    @Override
    public Optional<Game> loadBoardWithoutPlayer() {
        return template.opsForHash()
            .entries(BOARD_HASH_KEY)
            .values()
            .stream()
            .map(board -> (GameEntity) board)
            .map(BoardMapper::toDomain)
            .filter(board -> board.gameInfo().blackPlayer() == null)
            .findFirst();
    }

    @Override
    public Optional<Game> loadBoardByUsername(Username username) {
        return template.opsForHash()
            .entries(BOARD_HASH_KEY)
            .values()
            .stream()
            .map(board -> (GameEntity) board)
            .map(BoardMapper::toDomain)
            .filter(
                board -> Objects.equals(board.gameInfo().whitePlayer().username(), username)
                || (
                    board.gameInfo().blackPlayer()!= null
                    && Objects.equals(board.gameInfo().blackPlayer().username(), username)
                )
            )
            .findFirst();
    }

    @Override
    public GameId saveBoard(Game savedGame) {
        GameEntity savedEntity = BoardMapper.toEntity(savedGame);
        template.opsForHash().put(BOARD_HASH_KEY, savedEntity.gameId().toString(), savedEntity);
        return savedGame.gameId();
    }

    @Override
    public void deleteBoard(GameId gameId) {
        template.opsForHash().delete(BOARD_HASH_KEY, gameId.uuid().toString());
    }
}
