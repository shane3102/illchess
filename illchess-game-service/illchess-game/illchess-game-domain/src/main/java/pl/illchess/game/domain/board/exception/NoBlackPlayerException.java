package pl.illchess.game.domain.board.exception;

import pl.illchess.game.domain.commons.exception.BadRequestException;

public class NoBlackPlayerException extends BadRequestException {
    public NoBlackPlayerException() {
        super("Black player is not present");
    }
}
