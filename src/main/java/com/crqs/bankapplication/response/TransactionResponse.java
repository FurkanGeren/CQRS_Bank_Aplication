package com.crqs.bankapplication.response;
import java.math.BigDecimal;

public class TransactionResponse {
    private String transactionType; // "DEPOSIT" or "WITHDRAW"
    private String amount;
    private String timestamp;
    private BigDecimal balance;

    public TransactionResponse(String transactionType, String amount, String timestamp, BigDecimal balance) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = timestamp;
        this.balance = balance;
    }

    public String getAmount() {
        return amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTimestamp() {
        return timestamp;
    }
}