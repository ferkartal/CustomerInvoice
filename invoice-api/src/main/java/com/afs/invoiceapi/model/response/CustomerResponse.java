package com.afs.invoiceapi.model.response;

import com.afs.invoiceapi.model.Customer;

import java.util.Date;

public class CustomerResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Date birthDate;
    private String phoneNumber;
    private String address;

    public static CustomerResponse from(Customer customer) {
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setId(customer.getId());
        customerResponse.setAddress(customer.getAddress());
        customerResponse.setBirthDate(customer.getBirthDate());
        customerResponse.setEmail(customer.getEmail());
        customerResponse.setFirstName(customer.getFirstName());
        customerResponse.setLastName(customer.getLastName());
        customerResponse.setPhoneNumber(customer.getPhoneNumber());
        return customerResponse;
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
