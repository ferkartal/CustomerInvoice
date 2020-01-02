package com.afs.invoiceapi.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CustomerRequest {
    @NotNull(message = "firstName.cannot.be.null")
    @Size(max = 30, message = "firstName.max")
    private String firstName;
    @NotNull(message = "lastName.cannot.be.null")
    @Size(max = 30, message = "lastName.max")
    private String lastName;
    @NotNull(message = "email.cannot.be.null")
    @Size(max = 255, message = "email.max")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "invalid.email")
    private String email;
    @NotNull(message = "password.cannot.be.null")
    @Size(max = 30, message = "password.max")
    private String password;
    @NotNull(message = "birthDate.cannot.be.null")
    private String birthDate;
    @NotNull(message = "phoneNumber.cannot.be.null")
    @Size(max = 30, message = "phoneNumber.max")
    @Pattern(regexp = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}", message = "invalid.phoneNumber")
    private String phoneNumber;
    @NotNull(message = "address.cannot.be.null")
    @Size(max = 255, message = "address.max")
    private String address;

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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
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
