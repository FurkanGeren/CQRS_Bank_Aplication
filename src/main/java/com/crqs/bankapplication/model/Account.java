package com.crqs.bankapplication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Account {

    @Id
    private String accountId;
    private BigDecimal balance;
    private String firstName;
    private String lastName;

    public Account(String accountId, BigDecimal balance, String firstName, String lastName) {
        this.accountId = accountId;
        this.balance = balance;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Account() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId) && Objects.equals(balance, account.balance) && Objects.equals(firstName, account.firstName) && Objects.equals(lastName, account.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, balance, firstName, lastName);
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
