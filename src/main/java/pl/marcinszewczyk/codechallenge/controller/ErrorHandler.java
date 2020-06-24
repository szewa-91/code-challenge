package pl.marcinszewczyk.codechallenge.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.marcinszewczyk.codechallenge.content.MessageIncorrectException;
import pl.marcinszewczyk.codechallenge.user.UserNotFoundException;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {UserNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFound(RuntimeException ex, WebRequest webRequest) {
        String body = ex.getMessage();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);
    }

    @ExceptionHandler(value = {MessageIncorrectException.class})
    protected ResponseEntity<Object> handleIncorrectMessage(RuntimeException ex, WebRequest webRequest) {
        String body = ex.getMessage();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }
}
