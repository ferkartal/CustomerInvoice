package com.afs.invoiceapi.service;

import com.afs.invoiceapi.exceptions.EmailNotUniqueException;
import com.afs.invoiceapi.model.Customer;
import com.afs.invoiceapi.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void it_should_create_customer() {
        //given
        String email = "email@email.com";

        Customer customer = new Customer();
        customer.setEmail(email);

        Mockito.when(customerRepository.emailIsValid(email)).thenReturn(true);

        //when && then
        assertDoesNotThrow(() -> customerService.createCustomer(customer));
    }

    @Test
    public void it_should_throw_email_not_unique_exception_when_retrieving_customer_if_email_is_already_taken() {
        //given
        String email = "email@email.com";

        Customer customer = new Customer();
        customer.setEmail(email);

        Mockito.when(customerRepository.emailIsValid(email)).thenReturn(false);

        //when
        EmailNotUniqueException exception = assertThrows(EmailNotUniqueException.class,
                () -> customerService.createCustomer(customer));

        //then
        assertEquals("email.is.not.unique", exception.getKey());
    }

}