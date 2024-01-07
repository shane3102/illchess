package pl.illchess.adapter.board.command.in.websocket.advice;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import pl.illchess.application.board.command.BoardManager;
import pl.illchess.application.commons.command.out.PublishEvent;
import pl.illchess.domain.board.event.MoveIllegal;
import pl.illchess.domain.board.exception.IllegalMoveException;
import pl.illchess.domain.board.exception.PieceCantMoveToGivenSquareException;
import pl.illchess.domain.board.exception.PieceColorIncorrectException;
import pl.illchess.domain.board.exception.PieceNotPresentOnGivenSquare;
import pl.illchess.domain.board.exception.TargetSquareOccupiedBySameColorPieceException;

// TODO obsłużyć pozostałe warunki
@ControllerAdvice
@AllArgsConstructor
public class BoardCommandControllerAdvice extends StompSubProtocolErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(BoardManager.class);

    private final PublishEvent eventPublisher;

    @MessageExceptionHandler({
            PieceColorIncorrectException.class,
            PieceNotPresentOnGivenSquare.class,
            TargetSquareOccupiedBySameColorPieceException.class,
            PieceCantMoveToGivenSquareException.class
    })
    public void illegalMoveExceptionHandler(IllegalMoveException domainException) {
        MoveIllegal moveIllegal = domainException.toMoveIllegalEvent();
        log.error("Error was handled: {}", domainException.getMessage());
        eventPublisher.publishDomainEvent(moveIllegal);
    }
}
