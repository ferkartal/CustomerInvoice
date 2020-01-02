package com.afs.invoiceapi.model.response;

import com.afs.invoiceapi.model.Invoice;
import com.afs.invoiceapi.model.InvoiceItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


class InvoiceResponseTest {

    @Test
    public void it_should_convert_invoice_to_response() throws ParseException {
        //given
        String id = "1";
        Date createdDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
        BigDecimal totalPrice = new BigDecimal("1.55");

        Integer amount = 1;
        BigDecimal price = new BigDecimal("1.55");
        String description = "invoiceItemDescription";
        BigDecimal tax = new BigDecimal("0.186");

        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setCreatedDate(createdDate);
        invoice.setTotalPrice(totalPrice);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setAmount(amount);
        invoiceItem.setPrice(price);
        invoiceItem.setDescription(description);
        invoiceItem.setTax(tax);
        invoice.setInvoiceItems(Arrays.asList(invoiceItem));

        //when
        InvoiceResponse from = InvoiceResponse.from(invoice);

        //then
        Assertions.assertEquals(id, from.getId(), id);
        Assertions.assertEquals(createdDate, from.getCreatedDate());
        Assertions.assertEquals(totalPrice, from.getTotalPrice());
        Assertions.assertNotNull(from.getInvoiceItems());
        Assertions.assertEquals(1, from.getInvoiceItems().size());
        Assertions.assertEquals(amount, from.getInvoiceItems().get(0).getAmount());
        Assertions.assertEquals(price, from.getInvoiceItems().get(0).getPrice());
        Assertions.assertEquals(description, from.getInvoiceItems().get(0).getDescription());
        Assertions.assertEquals(tax, from.getInvoiceItems().get(0).getTax());
    }
}