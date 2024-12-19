package com.crqs.bankapplication.common.commands.customer;

import com.crqs.bankapplication.common.enums.Sex;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Objects;

public class CreateCustomerCommand {

    @TargetAggregateIdentifier
    private final String customerId;
    private final String firstName;
    private final String lastName;
    private final String citizenID;
    private final Sex sex;
    private final String password;

    public CreateCustomerCommand(String customerId, String firstName, String lastName, String citizenID, Sex sex, String password) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.citizenID = citizenID;
        this.sex = sex;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateCustomerCommand that = (CreateCustomerCommand) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(citizenID, that.citizenID) && sex == that.sex && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, firstName, lastName, citizenID, sex, password);
    }

    public String getPassword() {
        return password;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCitizenID() {
        return citizenID;
    }

    public Sex getSex() {
        return sex;
    }
}
