package com.afs.invoiceapi.model;

import com.afs.invoiceapi.model.request.CustomerRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;


class CustomerTest {

    @Test
    public void it_should_convert_request_to_customer() {

        //given
        String password = "123";
        String address = "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522";
        String birthDate = "1992-09-11";
        String email = "email@email.com";
        String firstName = "Elon";
        String lastName = "Musk";
        String phoneNumber = "2575637401";

        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setPassword(password);
        customerRequest.setAddress(address);
        customerRequest.setBirthDate(birthDate);
        customerRequest.setEmail(email);
        customerRequest.setFirstName(firstName);
        customerRequest.setLastName(lastName);
        customerRequest.setPhoneNumber(phoneNumber);

        //when
        Customer from = Customer.from(customerRequest);

        //then
        Assertions.assertEquals(password, from.getPassword());
        Assertions.assertEquals(address, from.getAddress());
        Assertions.assertEquals(birthDate, new SimpleDateFormat("yyyy-MM-dd").format(from.getBirthDate()));
        Assertions.assertEquals(email, from.getEmail());
        Assertions.assertEquals(firstName, from.getFirstName());
        Assertions.assertEquals(lastName, from.getLastName());
        Assertions.assertEquals(phoneNumber, from.getPhoneNumber());
    }

    @Test
    public void it_should_convert_request_to_customer_when_birthDate_is_null() {

        //given
        String password = "123";
        String address = "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522";
        String email = "email@email.com";
        String firstName = "Elon";
        String lastName = "Musk";
        String phoneNumber = "2575637401";

        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setPassword(password);
        customerRequest.setAddress(address);
        customerRequest.setEmail(email);
        customerRequest.setFirstName(firstName);
        customerRequest.setLastName(lastName);
        customerRequest.setPhoneNumber(phoneNumber);

        //when
        Customer from = Customer.from(customerRequest);

        //then
        Assertions.assertEquals(password, from.getPassword());
        Assertions.assertEquals(address, from.getAddress());
        Assertions.assertEquals(email, from.getEmail());
        Assertions.assertEquals(firstName, from.getFirstName());
        Assertions.assertEquals(lastName, from.getLastName());
        Assertions.assertEquals(phoneNumber, from.getPhoneNumber());
    }
}