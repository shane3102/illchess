package pl.illchess.game.domain.game.model.state;

public enum GameResultCause {
    // some player won
    CHECKMATE,
    RESIGNATION,

    // draw,
    STALEMATE,
    INSUFFICIENT_MATERIAL,
    PLAYER_AGREEMENT
}
