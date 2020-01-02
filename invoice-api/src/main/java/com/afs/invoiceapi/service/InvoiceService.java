package com.afs.invoiceapi.service;

import com.afs.invoiceapi.model.Invoice;
import com.afs.invoiceapi.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    public void createInvoice(Invoice invoice) {
        invoiceRepository.createInvoice(invoice);
    }

    @Transactional
    public void updateInvoice(Invoice invoice) {
        invoiceRepository.updateInvoice(invoice);
    }

    @Transactional
    public void deleteInvoice(String invoiceId, String customerId) {
        invoiceRepository.deleteInvoice(invoiceId, customerId);
    }

    public void deleteInvoices(String customerId) {
        invoiceRepository.deleteInvoices(customerId);
    }

    public List<Invoice> retrieveInvoices(String customerId) {
        return invoiceRepository.retrieveInvoices(customerId);
    }

    public Invoice retrieveInvoice(String invoiceId, String customerId) {
        return invoiceRepository.retrieveInvoice(invoiceId, customerId);
    }
}
