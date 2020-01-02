package com.afs.invoiceapi.controller;

import com.afs.invoiceapi.model.Customer;
import com.afs.invoiceapi.model.Invoice;
import com.afs.invoiceapi.model.request.CustomerRequest;
import com.afs.invoiceapi.model.request.InvoiceRequest;
import com.afs.invoiceapi.model.request.UpdateCustomerRequest;
import com.afs.invoiceapi.model.response.CustomerResponse;
import com.afs.invoiceapi.model.response.InvoiceResponse;
import com.afs.invoiceapi.service.CustomerService;
import com.afs.invoiceapi.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = {"${accepted.url}"})
public class CustomerController {

    private final CustomerService customerService;
    private final InvoiceService invoiceService;

    public CustomerController(CustomerService customerService, InvoiceService invoiceService) {
        this.customerService = customerService;
        this.invoiceService = invoiceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        Customer customer = Customer.from(customerRequest);
        customerService.createCustomer(customer);
        return CustomerResponse.from(customerService.retrieveCustomer(customer.getId()));
    }

    @GetMapping("/{customerId}")
    public CustomerResponse retrieveCustomer(@PathVariable("customerId") String customerId) {
        return CustomerResponse.from(customerService.retrieveCustomer(customerId));
    }


    @PutMapping("/{customerId}")
    public CustomerResponse updateCustomer(@Valid @RequestBody UpdateCustomerRequest updateCustomerRequest,
                                           @PathVariable("customerId") String customerId) {
        customerService.updateCustomer(updateCustomerRequest.getFirstName(), updateCustomerRequest.getLastName(),
                updateCustomerRequest.getAddress(), customerId);
        return CustomerResponse.from(customerService.retrieveCustomer(customerId));
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("customerId") String customerId) {
        customerService.deleteCustomer(customerId);
    }

    @PostMapping("/{customerId}/invoices")
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponse createInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest,
                              @PathVariable("customerId") String customerId) {
        Invoice invoice = Invoice.from(invoiceRequest, customerId, UUID.randomUUID().toString());
        invoiceService.createInvoice(invoice);
        return InvoiceResponse.from(invoiceService.retrieveInvoice(invoice.getId(), customerId));
    }

    @PutMapping("/{customerId}/invoices/{invoiceId}")
    public InvoiceResponse updateInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest,
                              @PathVariable("customerId") String customerId,
                              @PathVariable("invoiceId") String invoiceId) {
        Invoice invoice = Invoice.from(invoiceRequest, customerId, invoiceId);
        invoiceService.updateInvoice(invoice);
        return InvoiceResponse.from(invoiceService.retrieveInvoice(invoiceId, customerId));
    }

    @DeleteMapping("/{customerId}/invoices/{invoiceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(@PathVariable("customerId") String customerId,
                              @PathVariable("invoiceId") String invoiceId) {
        invoiceService.deleteInvoice(invoiceId, customerId);
    }

    @GetMapping("/{customerId}/invoices")
    public List<InvoiceResponse> retrieveInvoices(@PathVariable("customerId") String customerId) {
        return invoiceService.retrieveInvoices(customerId)
                .stream()
                .map(InvoiceResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{customerId}/invoices/{invoiceId}")
    public InvoiceResponse retrieveInvoice(@PathVariable("customerId") String customerId,
                              @PathVariable("invoiceId") String invoiceId) {
        return InvoiceResponse.from(invoiceService.retrieveInvoice(invoiceId, customerId));
    }
}
