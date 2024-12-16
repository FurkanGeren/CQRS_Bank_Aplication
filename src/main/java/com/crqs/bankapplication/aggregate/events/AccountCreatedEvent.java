package com.crqs.bankapplication.aggregate.events;

import java.math.BigDecimal;
import java.util.Objects;

public class AccountCreatedEvent {
    private final String accountId;
    private final BigDecimal initialBalance;
    private final String userFirstName;
    private final String userLastName;

    public AccountCreatedEvent(String accountId, BigDecimal initialBalance, String userFirstName, String userLastName) {
        this.accountId = accountId;
        this.initialBalance = initialBalance;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountCreatedEvent that = (AccountCreatedEvent) o;
        return Objects.equals(accountId, that.accountId) && Objects.equals(initialBalance, that.initialBalance) && Objects.equals(userFirstName, that.userFirstName) && Objects.equals(userLastName, that.userLastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, initialBalance, userFirstName, userLastName);
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }
}
