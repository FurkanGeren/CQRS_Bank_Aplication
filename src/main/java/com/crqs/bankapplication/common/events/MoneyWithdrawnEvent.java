package com.crqs.bankapplication.common.events;

import java.math.BigDecimal;

public class MoneyWithdrawnEvent {
    private final String accountId;
    private final BigDecimal amount;
    private final BigDecimal balance;
    private final String description;

    public MoneyWithdrawnEvent(String accountId, BigDecimal amount, BigDecimal balance, String description) {
        this.accountId = accountId;
        this.amount = amount;
        this.balance = balance;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
