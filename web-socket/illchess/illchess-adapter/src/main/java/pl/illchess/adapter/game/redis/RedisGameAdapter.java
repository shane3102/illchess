package pl.illchess.adapter.game.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.adapter.game.redis.hash.GameStateHash;
import pl.illchess.application.ReadGameStateUseCase;
import pl.illchess.application.SaveGameStateUseCase;
import pl.illchess.application.game_state.out.ReadUnoccupiedGameUseCase;
import pl.illchess.domain.chess_board.ChessBoard;
import pl.illchess.domain.chess_board.ChessBoardId;
import pl.illchess.domain.game.GameId;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class RedisGameAdapter implements
        SaveGameStateUseCase,
        ReadGameStateUseCase,
        ReadUnoccupiedGameUseCase {

    @Autowired
    private RedisTemplate<String, GameStateHash> redisTemplate;

    @Override
    public ChessBoard saveChessBoard(ChessBoard chessBoard) {

        GameStateHash gameStateHash = new GameStateHash(
                UUID.randomUUID(),
                null,
                null,
                chessBoard);

        redisTemplate.opsForHash().put(GameStateHash.class.toString(), gameStateHash.id(), gameStateHash);

        chessBoard.setChessBoardId(new ChessBoardId(gameStateHash.id()));

        return chessBoard;
    }

    @Override
    public String readChessBoard(UUID chessBoardId) {
        return Objects.requireNonNull(redisTemplate.opsForHash().get(GameStateHash.class.toString(), chessBoardId)).toString();
    }

    @Override
    public GameId readUnoccupiedGame() {
        HashOperations<String, UUID, GameStateHash> hashOperations = redisTemplate.opsForHash();

        List<GameStateHash> values = hashOperations.values(GameStateHash.class.toString());

        return values.stream()
                .filter(game -> game.usernameBlack() == null || game.usernameWhite() == null)
                .map(game -> new GameId(game.id()))
                .findFirst()
                .orElse(null);
    }
}
