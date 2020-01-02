package com.afs.invoiceapi.service;

import com.afs.invoiceapi.exceptions.DomainNotFoundException;
import com.afs.invoiceapi.model.Invoice;
import com.afs.invoiceapi.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerServiceIT {
    @Autowired
    private CustomerRepository customerRepository;
    @SpyBean
    private InvoiceService invoiceService;

    @Autowired
    private CustomerService customerService;

    @Test
    @SqlGroup({
            @Sql(value = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void it_should_delete_customer() {
        //given
        String customerId = "customerId3";

        //when
        customerService.deleteCustomer(customerId);

        //then
        List<Invoice> invoices = invoiceService.retrieveInvoices(customerId);
        assertTrue(invoices.isEmpty());
        DomainNotFoundException exception = assertThrows(DomainNotFoundException.class,
                () -> customerRepository.retrieveCustomer(customerId));
        assertEquals("customer.not.found", exception.getKey());
        assertEquals(customerId, exception.getArgs()[0]);
    }

    @Test
    @SqlGroup({
            @Sql(value = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void it_should_not_delete_invoice_items_when_exception_occured_during_delete_customer() {
        //given
        String customerId = "customerId2";

        Mockito.doThrow(RuntimeException.class).when(invoiceService).deleteInvoices(customerId);

        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customerService.deleteCustomer(customerId));

        //then
        assertNotNull(exception);
        assertNotNull(customerRepository.retrieveCustomer(customerId));
        List<Invoice> invoices = invoiceService.retrieveInvoices(customerId);
        assertEquals(1, invoices.size());
    }

}
