package pl.illchess.adapter.board.command.in.websocket.advice;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import pl.illchess.adapter.commons.ErrorMessage;
import pl.illchess.application.board.command.BoardManager;
import pl.illchess.domain.board.exception.BoardNotFoundException;
import pl.illchess.domain.board.exception.PieceColorIncorrectException;
import pl.illchess.domain.board.exception.PieceNotPresentOnGivenSquare;
import pl.illchess.domain.board.exception.TargetSquareOccupiedBySameColorPieceException;
import pl.illchess.domain.commons.exception.DomainException;

@ControllerAdvice
@AllArgsConstructor
public class BoardCommandControllerAdvice extends StompSubProtocolErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(BoardManager.class);

    @MessageExceptionHandler({
            BoardNotFoundException.class,
            PieceColorIncorrectException.class,
            PieceNotPresentOnGivenSquare.class,
            TargetSquareOccupiedBySameColorPieceException.class
    })
    public ErrorMessage badRequestExceptionHandler(DomainException domainException) {
        ErrorMessage errorMessage = new ErrorMessage(
                domainException.getMessage()
        );
        log.error("Error was handled: {}", errorMessage);
        return errorMessage;
    }
}
