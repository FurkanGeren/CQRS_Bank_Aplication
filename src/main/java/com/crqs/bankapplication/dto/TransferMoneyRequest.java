package com.crqs.bankapplication.dto;

import java.math.BigDecimal;

public class TransferMoneyRequest {

    private String sourceAccountId;
    private String destinationAccountId;
    private BigDecimal amount;

    public void setSourceAccountId(String sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public void setDestinationAccountId(String destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public void setAmount(BigDecimal amount) {
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
