package pl.illchess.game.domain.commons.exception;

public abstract class BadRequestException extends DomainException{
    public BadRequestException(String message) {
        super(message);
    }
}
