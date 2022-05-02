package org.codereview.shortlinkservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

/**
 * Обработчик исключений на уровне контроллера
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handlerExceptionNotFound(NoSuchElementException e) {
        log.error("not found element: ", e);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handlerExceptionIllegalArgumentInRequest(IllegalArgumentException e) {
        log.error("bag argument request: ", e);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
