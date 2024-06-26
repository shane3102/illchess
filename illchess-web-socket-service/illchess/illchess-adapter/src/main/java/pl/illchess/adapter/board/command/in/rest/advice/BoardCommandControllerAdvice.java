package pl.illchess.adapter.board.command.in.rest.advice;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import pl.illchess.adapter.board.command.in.rest.dto.IllegalMoveResponse;
import pl.illchess.application.board.command.BoardManager;
import pl.illchess.application.commons.command.out.PublishEvent;
import pl.illchess.domain.board.exception.IllegalMoveException;

@ControllerAdvice
@AllArgsConstructor
public class BoardCommandControllerAdvice extends StompSubProtocolErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(BoardManager.class);

    private final PublishEvent eventPublisher;

    @ExceptionHandler({IllegalMoveException.class})
    public ResponseEntity<IllegalMoveResponse> illegalMoveRestExceptionHandler(IllegalMoveException illegalMoveException) {
        log.error("Error was handled: {}", illegalMoveException.getMessage());
        IllegalMoveResponse response = new IllegalMoveResponse(
            illegalMoveException.getBoardId().uuid(),
            illegalMoveException.getHighlightSquare().toString(),
            illegalMoveException.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
