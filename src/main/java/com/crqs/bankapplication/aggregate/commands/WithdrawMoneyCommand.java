package com.crqs.bankapplication.aggregate.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

public class WithdrawMoneyCommand {
    @TargetAggregateIdentifier
    private final String accountId;
    private final BigDecimal amount;

    public WithdrawMoneyCommand(String accountId, BigDecimal amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
