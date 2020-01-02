package com.afs.invoiceapi.repository;

import com.afs.invoiceapi.exceptions.DomainNotFoundException;
import com.afs.invoiceapi.model.Invoice;
import com.afs.invoiceapi.model.InvoiceItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class InvoiceRepositoryTestIT {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void it_should_create_invoice() throws ParseException {
        //given
        String id = "1";
        Date createdDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
        String customerId = "1";
        BigDecimal totalPrice = new BigDecimal("1.5500");

        Integer amount = 1;
        BigDecimal price = new BigDecimal("1.5500");
        String description = "invoiceItemDescription";
        BigDecimal tax = new BigDecimal("0.1860");

        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setCreatedDate(createdDate);
        invoice.setCustomerId(customerId);
        invoice.setTotalPrice(totalPrice);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setAmount(amount);
        invoiceItem.setPrice(price);
        invoiceItem.setDescription(description);
        invoiceItem.setTax(tax);
        invoice.setInvoiceItems(Arrays.asList(invoiceItem));

        //when
        invoiceRepository.createInvoice(invoice);

        //then
        Invoice createdInvoice = invoiceRepository.retrieveInvoice(id, customerId);
        assertEquals(id, createdInvoice.getId());
        assertEquals(customerId, createdInvoice.getCustomerId());
        assertEquals(createdDate, createdInvoice.getCreatedDate());
        assertEquals(totalPrice, createdInvoice.getTotalPrice());
        assertNotNull(createdInvoice.getInvoiceItems());
        assertEquals(createdInvoice.getInvoiceItems().size(), 1);
        assertEquals(amount, createdInvoice.getInvoiceItems().get(0).getAmount());
        assertEquals(price, createdInvoice.getInvoiceItems().get(0).getPrice());
        assertEquals(description, createdInvoice.getInvoiceItems().get(0).getDescription());
        assertEquals(tax, createdInvoice.getInvoiceItems().get(0).getTax());
    }

    @Test
    @SqlGroup({
            @Sql(value = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void it_should_update_invoice() throws ParseException {
        //given
        String id = "invoiceId1";
        Date createdDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
        String customerId = "customerId1";
        BigDecimal totalPrice = new BigDecimal("11.5400");

        Integer amount1 = 1;
        BigDecimal price1 = new BigDecimal("1.5500");
        String description1 = "invoiceItemDescription1";
        BigDecimal tax1 = new BigDecimal("0.1860");

        Integer amount2 = 2;
        BigDecimal price2 = new BigDecimal("4.9950");
        String description2 = "invoiceItemDescription2";
        BigDecimal tax2 = new BigDecimal("0.5994");

        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setCustomerId(customerId);
        invoice.setCreatedDate(createdDate);
        invoice.setTotalPrice(totalPrice);

        InvoiceItem invoiceItem1 = new InvoiceItem();
        invoiceItem1.setAmount(amount1);
        invoiceItem1.setPrice(price1);
        invoiceItem1.setDescription(description1);
        invoiceItem1.setTax(tax1);

        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setAmount(amount2);
        invoiceItem2.setPrice(price2);
        invoiceItem2.setDescription(description2);
        invoiceItem2.setTax(tax2);

        invoice.setInvoiceItems(Arrays.asList(invoiceItem1, invoiceItem2));

        //when
        invoiceRepository.updateInvoice(invoice);

        //then
        Invoice createdInvoice = invoiceRepository.retrieveInvoice(id, customerId);
        assertEquals(id, createdInvoice.getId());
        assertEquals(customerId, createdInvoice.getCustomerId());
        assertEquals(createdDate, createdInvoice.getCreatedDate());
        assertEquals(totalPrice, createdInvoice.getTotalPrice());
        assertNotNull(createdInvoice.getInvoiceItems());
        assertEquals(createdInvoice.getInvoiceItems().size(), 2);
        assertEquals(amount1, createdInvoice.getInvoiceItems().get(0).getAmount());
        assertEquals(price1, createdInvoice.getInvoiceItems().get(0).getPrice());
        assertEquals(description1, createdInvoice.getInvoiceItems().get(0).getDescription());
        assertEquals(tax1, createdInvoice.getInvoiceItems().get(0).getTax());
        assertEquals(amount2, createdInvoice.getInvoiceItems().get(1).getAmount());
        assertEquals(price2, createdInvoice.getInvoiceItems().get(1).getPrice());
        assertEquals(description2, createdInvoice.getInvoiceItems().get(1).getDescription());
        assertEquals(tax2, createdInvoice.getInvoiceItems().get(1).getTax());
    }

    @Test
    public void it_should_throw_domain_not_found_exception_when_updating_invoice_if_invoice_does_not_exist() {
        //given
        String id = "invalidInvoiceId";
        String customerId = "invalidCustomerId";
        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setCustomerId(customerId);

        //when
        DomainNotFoundException exception = assertThrows(DomainNotFoundException.class,
                () -> invoiceRepository.updateInvoice(invoice));

        //then
        assertEquals("invoice.not.found", exception.getKey());
        assertEquals(id, exception.getArgs()[0]);
        assertEquals(customerId, exception.getArgs()[1]);
    }


    @Test
    @SqlGroup({
            @Sql(value = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void it_should_delete_invoice() {
        //given
        String id = "invoiceId2";
        String customerId = "customerId2";

        //when
        invoiceRepository.deleteInvoice(id, customerId);

        //then
        DomainNotFoundException exception = assertThrows(DomainNotFoundException.class,
                () -> invoiceRepository.retrieveInvoice(id, customerId));
        assertNotNull(exception);
    }

    @Test
    public void it_should_delete_invoice_if_invoice_does_not_exist() {

        //given
        String id = "invalidInvoiceId";
        String customerId = "invalidCustomerId";

        //when
        invoiceRepository.deleteInvoice(id, customerId);

        //when & then
        DomainNotFoundException exception = assertThrows(DomainNotFoundException.class,
                () -> invoiceRepository.retrieveInvoice(id, customerId));
        assertNotNull(exception);
    }

    @Test
    @SqlGroup({
            @Sql(value = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void it_should_delete_invoices() {
        //given
        String customerId = "customerId3";

        //when
        invoiceRepository.deleteInvoices(customerId);

        //then
        List<Invoice> invoices = invoiceRepository.retrieveInvoices(customerId);
        assertTrue(invoices.isEmpty());
    }

    @Test
    @SqlGroup({
            @Sql(value = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void it_should_retrieve_invoices() {
        //given
        String customerId = "customerId3";

        //when
        List<Invoice> invoices = invoiceRepository.retrieveInvoices(customerId);

        //then
        assertNotNull(invoices);
        assertEquals(2, invoices.size());
        Invoice invoice1 = invoices.get(0);
        Invoice invoice2 = invoices.get(1);
        assertEquals(new BigDecimal("1.5500"), invoice1.getTotalPrice());
        assertEquals(new BigDecimal("11.5400"), invoice2.getTotalPrice());
    }

    @Test
    @SqlGroup({
            @Sql(value = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void it_should_retrieve_invoice() {
        //given
        String id = "invoiceId3";
        String customerId = "customerId3";

        //when
        Invoice invoice = invoiceRepository.retrieveInvoice(id, customerId);

        //then
        assertNotNull(invoice);
        assertEquals(new BigDecimal("1.5500"), invoice.getTotalPrice());
        assertEquals(id, invoice.getId());
        assertEquals(customerId, invoice.getCustomerId());
        assertEquals(1, invoice.getInvoiceItems().size());
    }

    @Test
    public void it_should_throw_domain_not_found_exception_when_retrieving_invoice_if_invoice_does_not_exist() {
        //given
        String id = "invalidInvoiceId";
        String customerId = "invalidCustomerId";

        //when
        DomainNotFoundException exception = assertThrows(DomainNotFoundException.class,
                () -> invoiceRepository.retrieveInvoice(id, customerId));

        //then
        assertEquals("invoice.not.found", exception.getKey());
        assertEquals(id, exception.getArgs()[0]);
        assertEquals(customerId, exception.getArgs()[1]);
    }
}