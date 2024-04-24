package com.mstftrgt.ebank.exception;


import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountAccessDeniedException extends AccessDeniedException {

    public AccountAccessDeniedException(String msg) {
        super(msg);
    }
}
