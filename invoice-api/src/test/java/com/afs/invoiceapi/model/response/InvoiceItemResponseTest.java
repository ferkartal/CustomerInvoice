package com.afs.invoiceapi.model.response;

import com.afs.invoiceapi.model.InvoiceItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class InvoiceItemResponseTest {

    @Test
    public void it_should_convert_invoice_item_to_response() {
        //given
        Integer amount = 1;
        BigDecimal price = new BigDecimal("1.55");
        String description = "invoiceItemDescription";
        BigDecimal tax = new BigDecimal("0.186");

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setAmount(amount);
        invoiceItem.setPrice(price);
        invoiceItem.setDescription(description);
        invoiceItem.setTax(tax);

        //when
        InvoiceItemResponse from = InvoiceItemResponse.from(invoiceItem);

        //then
        Assertions.assertEquals(amount, from.getAmount());
        Assertions.assertEquals(price, from.getPrice());
        Assertions.assertEquals(description, from.getDescription());
        Assertions.assertEquals(tax, from.getTax());

    }
}