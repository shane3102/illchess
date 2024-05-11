package pl.illchess.domain.board.model.state;

import pl.illchess.domain.board.command.AcceptDraw;
import pl.illchess.domain.board.command.ProposeDraw;
import pl.illchess.domain.board.command.RejectDraw;
import pl.illchess.domain.board.command.Resign;
import pl.illchess.domain.board.exception.GameCanNotBeAcceptedOrRejectedAsDrawnException;
import pl.illchess.domain.board.exception.GameIsNotContinuableException;
import pl.illchess.domain.board.exception.InvalidUserProposingDrawException;
import pl.illchess.domain.board.exception.InvalidUserResigningGameException;
import pl.illchess.domain.board.exception.PieceColorIncorrectException;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.player.IsProposingDraw;
import pl.illchess.domain.board.model.state.player.Player;
import pl.illchess.domain.board.model.state.player.Username;
import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.info.CurrentPlayerColor;
import pl.illchess.domain.piece.model.info.PieceColor;

import java.util.Objects;

import static pl.illchess.domain.board.model.state.GameState.CONTINUE;
import static pl.illchess.domain.board.model.state.GameState.DRAW;
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
        if (gameState != CONTINUE) {
            throw new GameIsNotContinuableException(gameState);
        }

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
        if (gameState != CONTINUE) {
            throw new GameIsNotContinuableException(gameState);
        }
        if (Objects.equals(command.username(), blackPlayer.username())) {
            this.victoriousPlayerColor = WHITE;
        } else if (Objects.equals(command.username(), whitePlayer.username())) {
            this.victoriousPlayerColor = BLACK;
        } else {
            throw new InvalidUserResigningGameException(command.username());
        }

        this.gameState = RESIGNED;
    }

    public void proposeDraw(ProposeDraw command) {
        if (gameState != CONTINUE) {
            throw new GameIsNotContinuableException(gameState);
        }
        if (Objects.equals(command.username(), blackPlayer.username())) {
            blackPlayer.proposeDraw(command);
        } else if (Objects.equals(command.username(), whitePlayer.username())) {
            whitePlayer.proposeDraw(command);
        } else {
            throw new InvalidUserProposingDrawException(command.username());
        }
    }

    public void acceptDrawOffer(AcceptDraw command) {
        if (gameState != CONTINUE) {
            throw new GameIsNotContinuableException(gameState);
        }
        boolean isWhiteProposingDraw = Objects.equals(command.username(), blackPlayer.username()) && whitePlayer.isProposingDraw().value();
        boolean isBlackProposingDraw = Objects.equals(command.username(), whitePlayer.username()) && blackPlayer.isProposingDraw().value();
        if (isBlackProposingDraw || isWhiteProposingDraw) {
            blackPlayer.resetProposingDraw();
            whitePlayer.resetProposingDraw();
            gameState = DRAW;
        } else {
            throw new GameCanNotBeAcceptedOrRejectedAsDrawnException(command.boardId());
        }
    }

    public void rejectDrawOffer(RejectDraw command) {
        if (gameState != CONTINUE) {
            throw new GameIsNotContinuableException(gameState);
        }
        if (Objects.equals(command.username(), blackPlayer.username()) && whitePlayer.isProposingDraw().value()) {
            whitePlayer.resetProposingDraw();
        } else if (Objects.equals(command.username(), whitePlayer.username()) && blackPlayer.isProposingDraw().value()) {
            blackPlayer.resetProposingDraw();
        } else {
            throw new GameCanNotBeAcceptedOrRejectedAsDrawnException(command.boardId());
        }
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
            new Player(username, new IsProposingDraw(false)),
            null,
            CONTINUE,
            null
        );
    }

    public void setBlackPlayer(Player player) {
        this.blackPlayer = player;
    }

}