package com.mstftrgt.ebank.exception.handler;

import com.mstftrgt.ebank.exception.AccountNotFoundException;
import com.mstftrgt.ebank.exception.CustomerNotFoundException;
import com.mstftrgt.ebank.exception.InsufficientBalanceException;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected Map<String, List<String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, List<String>> body = new HashMap<>();

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);

        return body;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException.class)
    protected Map<String, String> handleAccountNotFoundException(AccountNotFoundException exception) {
        Map<String, String> map =  new HashMap<>();
        map.put("errorMessage", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    protected Map<String, String> handleCustomerNotFoundException(CustomerNotFoundException exception) {
        Map<String, String> map =  new HashMap<>();
        map.put("errorMessage", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InsufficientBalanceException.class)
    protected Map<String, String> handleInsufficientBalanceException(InsufficientBalanceException exception) {
        Map<String, String> map =  new HashMap<>();
        map.put("errorMessage", exception.getMessage());
        return map;
    }

}

