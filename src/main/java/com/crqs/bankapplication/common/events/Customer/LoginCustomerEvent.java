package com.crqs.bankapplication.common.events.Customer;

import java.util.Objects;

public class LoginCustomerEvent {

    private final String citizenID;
    private final String password;

    public LoginCustomerEvent(String citizenID, String password) {
        this.citizenID = citizenID;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginCustomerEvent that = (LoginCustomerEvent) o;
        return Objects.equals(citizenID, that.citizenID) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(citizenID, password);
    }

    public String getPassword() {
        return password;
    }

    public String getCitizenID() {
        return citizenID;
    }
}
