package pl.illchess.domain.board.model.state;

import pl.illchess.domain.piece.model.info.CurrentPlayerColor;
import pl.illchess.domain.piece.model.info.PieceColor;

public class BoardState {
    private final CurrentPlayerColor currentPlayerColor;
    private GameState gameState;
    private PieceColor victoriousPlayerColor;

    private BoardState(
            CurrentPlayerColor currentPlayerColor,
            GameState gameState,
            PieceColor victoriousPlayerColor
    ) {
        this.currentPlayerColor = currentPlayerColor;
        this.gameState = gameState;
        this.victoriousPlayerColor = victoriousPlayerColor;
    }

    public void changeState(GameState gameState) {
        this.gameState = gameState;
        if (gameState == GameState.CHECKMATE) {
            this.victoriousPlayerColor = currentPlayerColor.color().inverted();
        }
    }

    public void invertCurrentPlayerColor() {
        currentPlayerColor.invert();
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

    public static BoardState of(
            PieceColor currentPlayerColor,
            GameState gameState,
            PieceColor victoriousPlayerColor
    ) {
        return new BoardState(
                new CurrentPlayerColor(currentPlayerColor),
                gameState,
                victoriousPlayerColor
        );
    }

    public static BoardState defaultState() {
        return new BoardState(
                new CurrentPlayerColor(PieceColor.WHITE),
                GameState.CONTINUE,
                null
        );
    }
}
