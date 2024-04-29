package com.mstftrgt.ebank.exception.handler;

import com.mstftrgt.ebank.exception.AccountNotFoundException;
import com.mstftrgt.ebank.exception.CustomerNotFoundException;
import com.mstftrgt.ebank.exception.EmailAlreadyInUseException;
import com.mstftrgt.ebank.exception.InsufficientBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException.class)
    protected Map<String, String> handleAccountNotFoundException(AccountNotFoundException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("errorMessage", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    protected Map<String, String> handleCustomerNotFoundException(CustomerNotFoundException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("errorMessage", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InsufficientBalanceException.class)
    protected Map<String, String> handleInsufficientBalanceException(InsufficientBalanceException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("errorMessage", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailAlreadyInUseException.class)
    protected Map<String, String> handleEmailAlreadyInUseException(EmailAlreadyInUseException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("errorMessage", exception.getMessage());
        return map;
    }

}

