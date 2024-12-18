package com.crqs.bankapplication.query.entity;

import com.crqs.bankapplication.common.enums.Sex;
import jakarta.persistence.*;

@Entity
public class Customer {

    @Id
    private String id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String citizenID;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Account account;


    public Customer(String id, String firstName, String lastName, String citizenID, Sex sex, Account account) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.citizenID = citizenID;
        this.sex = sex;
        this.account = account;
    }

    public Customer() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getId() {
        return id;
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

    public Account getAccount() {
        return account;
    }
}
