package pl.illchess.game.adapter.board.command.in.rest.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import pl.illchess.game.adapter.board.command.in.rest.dto.IllegalMoveResponse;
import pl.illchess.game.application.game.command.GameManager;
import pl.illchess.game.domain.game.exception.GameNotFoundException;
import pl.illchess.game.domain.game.exception.IllegalMoveException;
import pl.illchess.game.domain.commons.exception.DomainException;
import pl.illchess.game.domain.commons.model.ErrorMessage;

@RestControllerAdvice
public class BoardCommandControllerAdvice extends StompSubProtocolErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(GameManager.class);

    @ExceptionHandler({IllegalMoveException.class})
    public ResponseEntity<IllegalMoveResponse> illegalMoveRestExceptionHandler(IllegalMoveException illegalMoveException) {
        log.error("Error was handled: {}", illegalMoveException.getMessage());
        IllegalMoveResponse response = new IllegalMoveResponse(
            illegalMoveException.getGameId().uuid(),
            illegalMoveException.getHighlightSquare().toString(),
            illegalMoveException.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
        GameNotFoundException.class
    })
    public ResponseEntity<ErrorMessage> notFoundExceptionHandler(DomainException domainException) {
        log.error("Error was handled: {}", domainException.getMessage());
        return new ResponseEntity<>(new ErrorMessage(domainException.getMessage()), HttpStatus.NOT_FOUND);
    }
}
