package com.afs.invoiceapi.model;

import com.afs.invoiceapi.model.request.InvoiceItemRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvoiceItemTest {

    @Test
    public void it_should_convert_request_to_invoice_item() {

        //given
        Integer amount = 1;
        BigDecimal price = new BigDecimal("1.55");
        String description = "invoiceItemDescription";

        InvoiceItemRequest invoiceItemRequest = new InvoiceItemRequest();
        invoiceItemRequest.setAmount(amount);
        invoiceItemRequest.setPrice(price);
        invoiceItemRequest.setDescription(description);

        //when
        InvoiceItem from = InvoiceItem.from(invoiceItemRequest);

        //then
        assertEquals(amount, from.getAmount());
        assertEquals(price, from.getPrice());
        assertEquals(description, from.getDescription());
        assertEquals(new BigDecimal("0.1860"), from.getTax());

    }

}