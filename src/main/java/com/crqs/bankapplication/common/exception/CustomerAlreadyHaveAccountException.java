package com.crqs.bankapplication.common.exception;

public class CustomerAlreadyHaveAccountException extends RuntimeException {
    public CustomerAlreadyHaveAccountException(String message) {
        super(message);
    }
}
