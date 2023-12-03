package pl.illchess.adapter.board.command.in.websocket.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.illchess.adapter.commons.ErrorMessage;
import pl.illchess.application.board.command.BoardManager;
import pl.illchess.domain.board.exception.BoardNotFoundException;
import pl.illchess.domain.board.exception.PieceColorIncorrectException;
import pl.illchess.domain.board.exception.PieceNotPresentOnGivenSquare;
import pl.illchess.domain.board.exception.TargetSquareOccupiedBySameColorPieceException;
import pl.illchess.domain.commons.exception.DomainException;

@ControllerAdvice
public class BoardCommandControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(BoardManager.class);

    @MessageExceptionHandler({
            BoardNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage notFoundExceptionHandler(DomainException domainException) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                domainException.getMessage()
        );
        log.error("Not found error was handled: {}", errorMessage);
        return errorMessage;
    }

    @MessageExceptionHandler({
            PieceColorIncorrectException.class,
            PieceNotPresentOnGivenSquare.class,
            TargetSquareOccupiedBySameColorPieceException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage badRequestExceptionHandler(DomainException domainException) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                domainException.getMessage()
        );
        log.error("Bad request error was handled: {}", errorMessage);
        return errorMessage;
    }
}
