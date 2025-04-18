package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.commons.exception.BadRequestException;

public class BoardFenPositionCouldNotBeEstablishedException extends BadRequestException {
    public BoardFenPositionCouldNotBeEstablishedException() {
        super("Fen position of board could not be established");
    }
}
