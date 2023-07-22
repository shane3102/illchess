package pl.illchess.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.application.ReadChessBoardStateUseCase;
import pl.illchess.application.SaveChessBoardStateUseCase;
import pl.illchess.domain.ChessBoard;
import pl.illchess.domain.ChessBoardId;

import java.util.Objects;
import java.util.UUID;

@Repository
public class RedisChessBoardAdapter implements SaveChessBoardStateUseCase, ReadChessBoardStateUseCase {

    @Autowired
    private RedisTemplate<String, ChessBoardHash> redisTemplate;

    @Override
    public ChessBoard saveChessBoard(ChessBoard chessBoard) {

        ChessBoardHash chessBoardHash = new ChessBoardHash(UUID.randomUUID(), chessBoard);

        redisTemplate.opsForHash().put(ChessBoardHash.class.toString(), chessBoardHash.id(), chessBoardHash);

        chessBoard.setChessBoardId(new ChessBoardId(chessBoardHash.id()));

        return chessBoard;
    }

    @Override
    public String readChessBoard(UUID chessBoardId) {
        return Objects.requireNonNull(redisTemplate.opsForHash().get(ChessBoardHash.class.toString(), chessBoardId)).toString();
    }
}
