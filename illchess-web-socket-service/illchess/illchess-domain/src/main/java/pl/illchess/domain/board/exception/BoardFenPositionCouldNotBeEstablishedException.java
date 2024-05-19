package pl.illchess.domain.board.exception;

import pl.illchess.domain.commons.exception.BadRequestException;

public class BoardFenPositionCouldNotBeEstablishedException extends BadRequestException {
    public BoardFenPositionCouldNotBeEstablishedException() {
        super("Fen position of board could not be established");
    }
}
