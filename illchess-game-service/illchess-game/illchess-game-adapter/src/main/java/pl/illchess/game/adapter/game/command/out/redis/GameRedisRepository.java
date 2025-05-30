package pl.illchess.game.adapter.game.command.out.redis;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.game.adapter.game.command.out.redis.mapper.GameMapper;
import pl.illchess.game.adapter.game.command.out.redis.model.GameEntity;
import pl.illchess.game.application.game.command.out.DeleteGame;
import pl.illchess.game.application.game.command.out.LoadGame;
import pl.illchess.game.application.game.command.out.SaveGame;
import pl.illchess.game.domain.game.model.Game;
import pl.illchess.game.domain.game.model.GameId;

import java.util.Optional;
import pl.illchess.game.domain.game.model.state.player.Username;

@Repository
@RequiredArgsConstructor
public class GameRedisRepository implements SaveGame, LoadGame, DeleteGame {

    private static final String BOARD_HASH_KEY = "BOARD";

    private final RedisTemplate<String, GameEntity> template;

    @Override
    public Optional<Game> loadGame(GameId gameId) {

        GameEntity readGameEntity = (GameEntity) template.opsForHash().get(BOARD_HASH_KEY, gameId.uuid().toString());

        Game game = GameMapper.toDomain(readGameEntity);

        return Optional.ofNullable(game);
    }

    @Override
    public Optional<Game> loadGameWithoutPlayer() {
        return template.opsForHash()
            .entries(BOARD_HASH_KEY)
            .values()
            .stream()
            .map(board -> (GameEntity) board)
            .map(GameMapper::toDomain)
            .filter(board -> board.gameInfo().blackPlayer() == null)
            .findFirst();
    }

    @Override
    public Optional<Game> loadGameByUsername(Username username) {
        return template.opsForHash()
            .entries(BOARD_HASH_KEY)
            .values()
            .stream()
            .map(board -> (GameEntity) board)
            .map(GameMapper::toDomain)
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
        GameEntity savedEntity = GameMapper.toEntity(savedGame);
        template.opsForHash().put(BOARD_HASH_KEY, savedEntity.gameId().toString(), savedEntity);
        return savedGame.gameId();
    }

    @Override
    public void deleteBoard(GameId gameId) {
        template.opsForHash().delete(BOARD_HASH_KEY, gameId.uuid().toString());
    }
}
