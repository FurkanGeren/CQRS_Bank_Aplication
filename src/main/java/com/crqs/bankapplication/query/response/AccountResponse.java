package com.crqs.bankapplication.query.response;

import java.math.BigDecimal;

public class AccountResponse {
    private String accountId;
    private BigDecimal balance;

    public AccountResponse(String accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
