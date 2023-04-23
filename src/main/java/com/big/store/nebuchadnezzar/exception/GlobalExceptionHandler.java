package com.big.store.nebuchadnezzar.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException exception) {
        ExceptionDetails errorDetails = new ExceptionDetails(LocalDateTime.now(), exception.getMessage(), HttpStatus.NOT_FOUND.value());
        logger.error("ProductNotFoundException was thrown {} -- {}", exception, errorDetails.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException exception) {
        ExceptionDetails errorDetails = new ExceptionDetails(LocalDateTime.now(), exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        logger.error("IllegalArgumentException was thrown {} -- {}", exception, errorDetails.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NoHandlerFoundException exception) {
        ExceptionDetails errorDetails = new ExceptionDetails(LocalDateTime.now(), "This is not the page you're looking for.", HttpStatus.NOT_FOUND.value());
        logger.error("NotFoundException was thrown {} -- {}", exception, errorDetails.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        ExceptionDetails errorDetails = new ExceptionDetails(LocalDateTime.now(), exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("Exception was thrown {} -- {}", exception, errorDetails.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }
}
