package com.afs.invoiceapi.repository;

import com.afs.invoiceapi.exceptions.DomainNotFoundException;
import com.afs.invoiceapi.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CustomerRepositoryTestIT {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void it_should_create_customer() throws ParseException {
        //given
        String id = "1";
        String address = "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522";
        String email = "email@email.com";
        Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse("1992-09-11");
        String firstName = "Elon";
        String lastName = "Musk";
        String phoneNumber = "2575637401";
        String password = "123";

        Customer customer = new Customer();
        customer.setId(id);
        customer.setAddress(address);
        customer.setEmail(email);
        customer.setBirthDate(birthDate);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhoneNumber(phoneNumber);
        customer.setPassword(password);

        //when
        assertDoesNotThrow(() -> customerRepository.createCustomer(customer));

        //then
        Customer retrievedCustomer = customerRepository.retrieveCustomer(customer.getId());
        assertEquals(customer.getId(), retrievedCustomer.getId());
        assertEquals(customer.getFirstName(), retrievedCustomer.getFirstName());
        assertEquals(customer.getLastName(), retrievedCustomer.getLastName());
        assertEquals(customer.getAddress(), retrievedCustomer.getAddress());
        assertEquals(customer.getPhoneNumber(), retrievedCustomer.getPhoneNumber());
        assertEquals(customer.getEmail(), retrievedCustomer.getEmail());
        assertEquals(customer.getBirthDate(), retrievedCustomer.getBirthDate());

    }

    @Test
    @SqlGroup({
            @Sql(value = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void it_should_retrieve_customer() throws ParseException {
        //given
        String customerId = "customerId1";

        //when
        Customer retrievedCustomer = customerRepository.retrieveCustomer(customerId);

        //then
        assertEquals(customerId, retrievedCustomer.getId());
        assertEquals("firstName1", retrievedCustomer.getFirstName());
        assertEquals("lastName1", retrievedCustomer.getLastName());
        assertEquals("address1", retrievedCustomer.getAddress());
        assertEquals("9999999998", retrievedCustomer.getPhoneNumber());
        assertEquals("email1@email.com", retrievedCustomer.getEmail());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("1992-09-11"), retrievedCustomer.getBirthDate());
    }

    @Test
    public void it_should_throw_domain_not_found_exception_when_retrieving_customer() {
        //given
        String customerId = "invalidCustomerId";

        //when
        DomainNotFoundException exception = assertThrows(DomainNotFoundException.class, () -> customerRepository.retrieveCustomer(customerId));

        //then
        assertEquals("customer.not.found", exception.getKey());
        assertEquals(customerId, exception.getArgs()[0]);
    }

    @Test
    @SqlGroup({
            @Sql(value = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void it_should_retrieve_user() {
        //given
        String email = "email1@email.com";

        //when
        User user = customerRepository.retrieveUser(email);

        //then
        assertEquals(email, user.getUsername());
        assertEquals("encryptedPassword1", user.getPassword());
        assertEquals(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")), user.getAuthorities());
    }


    @Test
    @SqlGroup({
            @Sql(value = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void it_should_update_customer() {
        //given
        String customerId = "customerId2";
        String address = "new address";
        String firstName = "updatedFirstName";
        String lastName = "updatedLastName";

        //when
        customerRepository.updateCustomer(firstName, lastName, address, customerId);

        //then
        Customer retrievedCustomer = customerRepository.retrieveCustomer(customerId);
        assertEquals(customerId, retrievedCustomer.getId());
        assertEquals(firstName, retrievedCustomer.getFirstName());
        assertEquals(lastName, retrievedCustomer.getLastName());
        assertEquals(address, retrievedCustomer.getAddress());
        assertEquals("9999999997", retrievedCustomer.getPhoneNumber());
    }

    @Test
    @SqlGroup({
            @Sql(value = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void it_should_delete_customer() {
        //given
        String customerId = "customerId3";

        //when
        customerRepository.deleteCustomer(customerId);

        //then
        DomainNotFoundException exception = assertThrows(DomainNotFoundException.class,
                () -> customerRepository.retrieveCustomer(customerId));
        assertEquals("customer.not.found", exception.getKey());
        assertEquals(customerId, exception.getArgs()[0]);
    }
}