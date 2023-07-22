package pl.illchess.adapter;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import pl.illchess.domain.ChessBoard;

import java.io.Serializable;
import java.util.UUID;

//TODO refactor nie używaj obiektu z domeny głąbie
@RedisHash("ChessBoardEntity")
public record ChessBoardHash(@Id UUID id, ChessBoard chessBoard) implements Serializable {

}
