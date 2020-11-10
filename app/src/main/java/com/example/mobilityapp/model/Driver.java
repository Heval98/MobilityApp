package com.example.mobilityapp.model;

public class Driver {

    private String uid;
    private String name_driver;
    private String last_name_driver;
    private String Email;

    private String document_person;
    private String address_person;
    private String property_card;
    private String bank_account;
    private String password_person;

    public Driver() {
    }

    public String getDocument_person() {
        return document_person;
    }

    public void setDocument_person(String document_person) {
        this.document_person = document_person;
    }

    public String getAddress_person() {
        return address_person;
    }

    public void setAddress_person(String address_person) {
        this.address_person = address_person;
    }

    public String getProperty_card() {
        return property_card;
    }

    public void setProperty_card(String property_card) {
        this.property_card = property_card;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public String getPassword_person() {
        return password_person;
    }

    public void setPassword_person(String password_person) {
        this.password_person = password_person;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName_driver() {
        return name_driver;
    }

    public void setName_driver(String name_driver) {
        this.name_driver = name_driver;
    }

    public String getLast_name_driver() {
        return last_name_driver;
    }

    public void setLast_name_driver(String last_name_driver) {
        this.last_name_driver = last_name_driver;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String toString() {
        return name_driver;
    }
}
