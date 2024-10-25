package pl.illchess.game.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.illchess.game.domain.commons.exception.BadRequestException;

@RestControllerAdvice
public class GlobalRestControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(GlobalRestControllerAdvice.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException badRequestException) {
        log.info("Handled new bad request exception. Exception message: {}", badRequestException.getMessage());
        return new ResponseEntity<>(badRequestException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
