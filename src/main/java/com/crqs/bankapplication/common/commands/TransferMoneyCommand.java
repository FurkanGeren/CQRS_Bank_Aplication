package com.crqs.bankapplication.common.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

public class TransferMoneyCommand {

    @TargetAggregateIdentifier
    private final String sourceAccountId;
    private final String destinationAccountId;
    private final BigDecimal amount;

    public TransferMoneyCommand(String sourceAccountId, String destinationAccountId, BigDecimal amount) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public String getDestinationAccountId() {
        return destinationAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
