package com.lucasdev.picpaysimplificado.controller.exceptions;

import com.lucasdev.picpaysimplificado.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> notFoundException(ResourceNotFoundException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND; //CODE 404
        String name = "Resource not found";
        String message = e.getMessage();
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BankException.class)
    public ResponseEntity<StandardError> bankException(BankException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; //CODE 500
        String name = "Bank Internal Error";
        String message = e.getMessage();
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BankNotificationException.class)
    public ResponseEntity<StandardError> bankExceptionNotify(BankNotificationException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE; //CODE 503
        String name = "Bank Internal notify Error";
        String message = e.getMessage();
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BankBadRequestException.class)
    public ResponseEntity<StandardError> bankBadRequest(BankBadRequestException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST; //CODE 400
        String name = "Bad Request";
        String message = e.getMessage();
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);

        return ResponseEntity.status(status).body(err);
    }


    @ExceptionHandler(BankUnauthorizedException.class)
    public ResponseEntity<StandardError> bankUnauthorizedException(BankUnauthorizedException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNAUTHORIZED; //CODE 401
        String name = "Request Unauthorized";
        String message = e.getMessage();
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> genericException(Exception e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; //CODE 500
        String name = "Unexpected Error";
        String message = "An unexpected error occurred.";
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) //for @Validate
    public ResponseEntity<StandardError> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; // CODE 422
        String name = "Validation Error";

        String message = e.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ":" + err.getDefaultMessage())
                .collect(Collectors.joining(";"));

        String path = request.getRequestURI();

        StandardError err = new StandardError(name,  message, status, Instant.now(), path);

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<StandardError> invalidArgsException(UnexpectedTypeException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; //CODE 422
        String name = "Validation Error";
        String message = "Invalid argument or violated integrity";
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BankViolationIntegrityException.class)
    public ResponseEntity<StandardError> integrityViolationBank(BankViolationIntegrityException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT; //CODE 409
        String name = "Integrity violation Error";
        String message = e.getMessage();
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BankIntegrityException.class)
    public ResponseEntity<StandardError> integrityBank(BankIntegrityException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT; //CODE 409
        String name = "Integrity Error";
        String message = e.getMessage();
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);

        return ResponseEntity.status(status).body(err);
    }

}