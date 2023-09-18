package pl.illchess.adapter.game.redis.hash;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import pl.illchess.domain.chess_board.ChessBoard;

import java.io.Serializable;
import java.util.UUID;

//TODO refactor nie używaj obiektu z domeny głąbie
@RedisHash("ChessGameStateHash")
public final class GameStateHash implements Serializable {
    @Id
    private UUID id;
    private String usernameWhite;
    private String usernameBlack;
    private ChessBoard chessBoard;

    public GameStateHash(
            UUID id,
            String usernameWhite,
            String usernameBlack,
            ChessBoard chessBoard
    ) {
        this.id = id;
        this.usernameWhite = usernameWhite;
        this.usernameBlack = usernameBlack;
        this.chessBoard = chessBoard;
    }

    @Id
    public UUID id() {
        return id;
    }

    public String usernameWhite() {
        return usernameWhite;
    }

    public String usernameBlack() {
        return usernameBlack;
    }

    public ChessBoard chessBoard() {
        return chessBoard;
    }

    public void setUsernameWhite(String usernameWhite) {
        this.usernameWhite = usernameWhite;
    }

    public void setUsernameBlack(String usernameBlack) {
        this.usernameBlack = usernameBlack;
    }

    @Override
    public String toString() {
        return "GameStateHash[" +
                "id=" + id + ", " +
                "usernameWhite=" + usernameWhite + ", " +
                "usernameBlack=" + usernameBlack + ", " +
                "chessBoard=" + chessBoard + ']';
    }


}
