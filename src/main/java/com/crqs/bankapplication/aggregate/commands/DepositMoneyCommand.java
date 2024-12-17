package com.crqs.bankapplication.aggregate.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

public class DepositMoneyCommand {

    @TargetAggregateIdentifier
    private final String accountId;
    private final BigDecimal amount;
    private final String description;

    public DepositMoneyCommand(String accountId, BigDecimal amount, String description) {
        this.accountId = accountId;
        this.amount = amount;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
