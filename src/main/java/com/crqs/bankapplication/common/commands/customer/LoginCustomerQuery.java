package com.crqs.bankapplication.common.commands.customer;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Objects;

public class LoginCustomerQuery {

    @TargetAggregateIdentifier
    private final String citizenID;
    private final String password;

    public LoginCustomerQuery(String citizenID, String password) {
        this.citizenID = citizenID;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginCustomerQuery that = (LoginCustomerQuery) o;
        return Objects.equals(citizenID, that.citizenID) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(citizenID, password);
    }

    public String getCitizenID() {
        return citizenID;
    }

    public String getPassword() {
        return password;
    }
}
