package com.crqs.bankapplication.common.commands.account;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.Objects;

public class CreateAccountCommand {

    @TargetAggregateIdentifier
    private final String accountId;
    private final BigDecimal initialBalance;
    private final String customerId;

    public CreateAccountCommand(String accountId, BigDecimal initialBalance, String customerId) {
        this.accountId = accountId;
        this.initialBalance = initialBalance;
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateAccountCommand that = (CreateAccountCommand) o;
        return Objects.equals(accountId, that.accountId) && Objects.equals(initialBalance, that.initialBalance) && Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, initialBalance, customerId);
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }
}
