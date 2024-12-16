package com.crqs.bankapplication.dto;


import java.math.BigDecimal;


public class DepositMoneyRequest {

    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}