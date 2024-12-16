package com.crqs.bankapplication.aggregate.events;

import java.math.BigDecimal;

public class MoneyReceivedEvent {

    private final String destinationAccountId;
    private final String sourceAccountId;
    private final BigDecimal amount;
    private final BigDecimal destinationBalance;

    public MoneyReceivedEvent(String destinationAccountId, String sourceAccountId, BigDecimal amount, BigDecimal destinationBalance) {
        this.destinationAccountId = destinationAccountId;
        this.sourceAccountId = sourceAccountId;
        this.amount = amount;
        this.destinationBalance = destinationBalance;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getDestinationBalance() {
        return destinationBalance;
    }

    public String getDestinationAccountId() {
        return destinationAccountId;
    }
}
