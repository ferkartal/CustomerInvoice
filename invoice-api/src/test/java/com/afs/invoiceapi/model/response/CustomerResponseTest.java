package com.afs.invoiceapi.model.response;

import com.afs.invoiceapi.model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
class CustomerResponseTest {

    @Test
    public void it_should_convert_customer_to_response() throws ParseException {
        //given
        String id = "1";
        String address = "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522";
        Date birthDate =  new SimpleDateFormat("yyyy-MM-dd").parse("1992-09-11");
        String email = "email@email.com";
        String firstName = "Elon";
        String lastName = "Musk";
        String phoneNumber = "2575637401";

        Customer customer = new Customer();
        customer.setId(id);
        customer.setAddress(address);
        customer.setBirthDate(birthDate);
        customer.setEmail(email);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhoneNumber(phoneNumber);

        //when
        CustomerResponse from = CustomerResponse.from(customer);

        //then
        Assertions.assertEquals(id, from.getId());
        Assertions.assertEquals(address, from.getAddress());
        Assertions.assertEquals(birthDate, from.getBirthDate());
        Assertions.assertEquals(email, from.getEmail());
        Assertions.assertEquals(firstName, from.getFirstName());
        Assertions.assertEquals(lastName, from.getLastName());
        Assertions.assertEquals(phoneNumber, from.getPhoneNumber());

    }

}