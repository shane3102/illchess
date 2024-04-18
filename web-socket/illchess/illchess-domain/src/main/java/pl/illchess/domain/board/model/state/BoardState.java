package pl.illchess.domain.board.model.state;

import pl.illchess.domain.board.command.Resign;
import pl.illchess.domain.board.exception.InvalidUserResigningGameException;
import pl.illchess.domain.board.exception.PieceColorIncorrectException;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.player.Player;
import pl.illchess.domain.board.model.state.player.Username;
import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.info.CurrentPlayerColor;
import pl.illchess.domain.piece.model.info.PieceColor;

import java.util.Objects;

import static pl.illchess.domain.board.model.state.GameState.RESIGNED;
import static pl.illchess.domain.piece.model.info.PieceColor.BLACK;
import static pl.illchess.domain.piece.model.info.PieceColor.WHITE;

public class BoardState {
    private final CurrentPlayerColor currentPlayerColor;
    private Player whitePlayer;
    private Player blackPlayer;
    private GameState gameState;
    private PieceColor victoriousPlayerColor;

    private BoardState(
        CurrentPlayerColor currentPlayerColor,
        Player whitePlayer,
        Player blackPlayer,
        GameState gameState,
        PieceColor victoriousPlayerColor
    ) {
        this.currentPlayerColor = currentPlayerColor;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.gameState = gameState;
        this.victoriousPlayerColor = victoriousPlayerColor;
    }

    public void changeState(GameState gameState) {
        this.gameState = gameState;
        if (gameState == GameState.CHECKMATE) {
            this.victoriousPlayerColor = currentPlayerColor.color().inverted();
        }
    }

    public void checkIfAllowedToMove(BoardId boardId, Piece movedPiece, Username usernamePerformingMove) {
        if (!Objects.equals(movedPiece.color(), currentPlayerColor().color())) {
            throw new PieceColorIncorrectException(
                boardId,
                movedPiece.color(),
                currentPlayerColor().color(),
                movedPiece.square()
            );
        }

        // TODO uncomment to allow only to move by username
//        Player validPlayer = switch (movedPiece.color()) {
//            case WHITE -> whitePlayer;
//            case BLACK -> blackPlayer;
//        };
//
//
//        if (!Objects.equals(validPlayer.username(), usernamePerformingMove)) {
//            throw new InvalidUserPerformedMoveException(
//                boardId,
//                movedPiece.square(),
//                usernamePerformingMove,
//                validPlayer.username(),
//                movedPiece.color()
//            );
//        }
    }

    public void invertCurrentPlayerColor() {
        currentPlayerColor.invert();
    }

    public void resign(Resign command) {
        if (Objects.equals(command.username(), blackPlayer.username())) {
            this.victoriousPlayerColor = WHITE;
        } else if (Objects.equals(command.username(), whitePlayer.username())) {
            this.victoriousPlayerColor = BLACK;
        } else {
            throw new InvalidUserResigningGameException(command.username());
        }

        this.gameState = RESIGNED;
    }

    public CurrentPlayerColor currentPlayerColor() {
        return currentPlayerColor;
    }

    public GameState gameState() {
        return gameState;
    }

    public PieceColor victoriousPlayerColor() {
        return victoriousPlayerColor;
    }

    public Player whitePlayer() {
        return whitePlayer;
    }

    public Player blackPlayer() {
        return blackPlayer;
    }

    public static BoardState of(
        PieceColor currentPlayerColor,
        GameState gameState,
        Player player1,
        Player player2,
        PieceColor victoriousPlayerColor
    ) {
        return new BoardState(
            new CurrentPlayerColor(currentPlayerColor),
            player1,
            player2,
            gameState,
            victoriousPlayerColor
        );
    }

    public static BoardState fromFenStringAndByUsername(
        FenString fenString,
        Username username
    ) {
        String movingColor = fenString.value().split(" ")[1];

        return new BoardState(
            new CurrentPlayerColor(movingColor.equals("b") ? PieceColor.BLACK : WHITE),
            new Player(username, WHITE),
            null,
            GameState.CONTINUE,
            null
        );
    }

    public void setBlackPlayer(Player player) {
        this.blackPlayer = player;
    }


}
