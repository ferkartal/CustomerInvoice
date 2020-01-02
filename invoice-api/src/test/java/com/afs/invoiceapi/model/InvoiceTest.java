package com.afs.invoiceapi.model;

import com.afs.invoiceapi.model.request.InvoiceItemRequest;
import com.afs.invoiceapi.model.request.InvoiceRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InvoiceTest {

    @Test
    public void it_should_convert_request_to_invoice() {

        //given
        String invoiceId = "1";
        String customerId = "1";
        String createdDate = "2020-01-01";

        Integer amount = 1;
        BigDecimal price = new BigDecimal("1.55");
        String description = "invoiceItemDescription";

        InvoiceItemRequest invoiceItemRequest = new InvoiceItemRequest();
        invoiceItemRequest.setAmount(amount);
        invoiceItemRequest.setPrice(price);
        invoiceItemRequest.setDescription(description);

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setCreatedDate(createdDate);
        invoiceRequest.setInvoiceItems(Collections.singletonList(invoiceItemRequest));

        //when
        Invoice from = Invoice.from(invoiceRequest, customerId, invoiceId);

        //then
        assertEquals(invoiceId, from.getId());
        assertEquals(customerId, from.getCustomerId());
        assertEquals(createdDate, new SimpleDateFormat("yyyy-MM-dd").format(from.getCreatedDate()));
        assertNotNull(from.getInvoiceItems());
        assertEquals(amount, from.getInvoiceItems().get(0).getAmount());
        assertEquals(price, from.getInvoiceItems().get(0).getPrice());
        assertEquals(description, from.getInvoiceItems().get(0).getDescription());
        assertEquals(new BigDecimal("0.1860"), from.getInvoiceItems().get(0).getTax());

    }

}