package com.crqs.bankapplication.common.exception;


import java.math.BigDecimal;

public class BalanceNotSufficientException extends RuntimeException {

    private final BigDecimal balance;

    public BalanceNotSufficientException(String message, BigDecimal balance) {
        super(message);
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
