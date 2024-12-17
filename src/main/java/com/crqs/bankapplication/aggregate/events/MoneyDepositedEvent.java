package com.crqs.bankapplication.aggregate.events;

import java.math.BigDecimal;

public class MoneyDepositedEvent {

    private final String accountId;
    private final BigDecimal amount;
    private final BigDecimal balance;
    private final String description;

    public MoneyDepositedEvent(String accountId, BigDecimal amount, BigDecimal balance, String description) {
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
