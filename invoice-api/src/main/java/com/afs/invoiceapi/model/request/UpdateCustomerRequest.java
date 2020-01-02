package com.afs.invoiceapi.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateCustomerRequest {
    @NotNull(message = "firstName.cannot.be.null")
    @Size(max = 30, message = "firstName.max")
    private String firstName;
    @NotNull(message = "lastName.cannot.be.null")
    @Size(max = 30, message = "lastName.max")
    private String lastName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
