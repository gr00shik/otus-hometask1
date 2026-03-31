package ru.otus.socialnetwork.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.socialnetwork.dto.ErrorResponse;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return status(NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), UUID.randomUUID().toString(), 404));
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePostNotFound(PostNotFoundException ex) {
        return status(NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), UUID.randomUUID().toString(), 404));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return status(BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage(), UUID.randomUUID().toString(), 400));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        return status(BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage(), UUID.randomUUID().toString(), 400));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return status(INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Internal server error", UUID.randomUUID().toString(), 500));
    }

}
