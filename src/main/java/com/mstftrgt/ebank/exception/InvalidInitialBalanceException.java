package com.mstftrgt.ebank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInitialBalanceException extends RuntimeException {
    public InvalidInitialBalanceException(String message) {
        super(message);
    }
}


