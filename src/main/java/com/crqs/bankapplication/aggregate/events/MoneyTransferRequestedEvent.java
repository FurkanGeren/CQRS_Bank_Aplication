package com.crqs.bankapplication.aggregate.events;

import java.math.BigDecimal;
import java.util.Objects;

public class MoneyTransferRequestedEvent {

    private final String transferId;
    private final String sourceAccountId;
    private final String destinationAccountId;
    private final BigDecimal amount;

    public MoneyTransferRequestedEvent(String transferId, String sourceAccountId, String destinationAccountId, BigDecimal amount) {
        this.transferId = transferId;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;

        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyTransferRequestedEvent that = (MoneyTransferRequestedEvent) o;
        return Objects.equals(transferId, that.transferId) && Objects.equals(sourceAccountId, that.sourceAccountId) && Objects.equals(destinationAccountId, that.destinationAccountId) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, sourceAccountId, destinationAccountId, amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTransferId() {
        return transferId;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public String getDestinationAccountId() {
        return destinationAccountId;
    }


}
