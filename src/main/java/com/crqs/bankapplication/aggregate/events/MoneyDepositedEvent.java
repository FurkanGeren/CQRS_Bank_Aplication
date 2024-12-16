package com.crqs.bankapplication.aggregate.events;

import java.math.BigDecimal;

public class MoneyDepositedEvent {

    private final String accountId;
    private final BigDecimal amount;
    private final BigDecimal balance;

    public MoneyDepositedEvent(String accountId, BigDecimal amount, BigDecimal balance) {
        this.accountId = accountId;
        this.amount = amount;
        this.balance = balance;
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
