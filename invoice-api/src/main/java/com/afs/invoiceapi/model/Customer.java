package com.afs.invoiceapi.model;

import com.afs.invoiceapi.exceptions.ValidationException;
import com.afs.invoiceapi.model.request.CustomerRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Customer {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Date birthDate;
    private String phoneNumber;
    private String address;

    public static Customer from(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID().toString());
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPassword(customerRequest.getPassword());
        customer.setBirthDate(getDate(customerRequest.getBirthDate()));
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customer.setAddress(customerRequest.getAddress());
        return customer;
    }

    private static Date getDate(String birthDate) {
        //birthDate validation
        if (birthDate == null)
            return null;
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(birthDate);
        } catch (ParseException e) {
            throw new ValidationException("invalid.date", birthDate);
        }
    }

    public static Customer from(String id, String firstName, String lastName, String email, Date birthDate,
                                String phoneNumber, String address) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setBirthDate(birthDate);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);
        return customer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

