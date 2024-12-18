package com.crqs.bankapplication.common.events;

import java.math.BigDecimal;

public class MoneySentEvent {

    private final String sourceAccountId;
    private final String destinationAccountId;
    private final BigDecimal amount;
    private final BigDecimal sourceBalance;

    public MoneySentEvent(String sourceAccountId, String destinationAccountId, BigDecimal amount, BigDecimal sourceBalance) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.sourceBalance = sourceBalance;
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

    public BigDecimal getSourceBalance() {
        return sourceBalance;
    }

}
