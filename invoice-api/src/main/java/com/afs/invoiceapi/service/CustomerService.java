package com.afs.invoiceapi.service;

import com.afs.invoiceapi.exceptions.EmailNotUniqueException;
import com.afs.invoiceapi.model.Customer;
import com.afs.invoiceapi.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final InvoiceService invoiceService;

    public CustomerService(CustomerRepository customerRepository, InvoiceService invoiceService) {
        this.customerRepository = customerRepository;
        this.invoiceService = invoiceService;
    }

    public void createCustomer(Customer customer) {
        if (customerRepository.emailIsValid(customer.getEmail())) {
            customerRepository.createCustomer(customer);
        } else {
            throw new EmailNotUniqueException("email.is.not.unique", customer.getEmail());
        }
    }

    public Customer retrieveCustomer(String customerId) {
        return customerRepository.retrieveCustomer(customerId);
    }

    public void updateCustomer(String firstName, String lastName, String address, String customerId) {
        customerRepository.updateCustomer(firstName, lastName, address, customerId);
    }

    @Transactional
    public void deleteCustomer(String customerId) {
        customerRepository.deleteCustomer(customerId);
        invoiceService.deleteInvoices(customerId);
    }

    public String retrieveCustomerId(String email) {
        return customerRepository.retrieveCustomerId(email);
    }
}
