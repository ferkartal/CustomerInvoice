package com.afs.invoiceapi.model;

import com.afs.invoiceapi.model.request.InvoiceItemRequest;

import java.math.BigDecimal;

public class InvoiceItem {

    private static final BigDecimal TAX = BigDecimal.valueOf(12);
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private Integer amount;
    private BigDecimal price;
    private String description;
    private BigDecimal tax;

    public static InvoiceItem from(InvoiceItemRequest invoiceItemRequest) {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setPrice(invoiceItemRequest.getPrice());
        invoiceItem.setAmount(invoiceItemRequest.getAmount());
        invoiceItem.setDescription(invoiceItemRequest.getDescription());
        invoiceItem.setTax(invoiceItemRequest.getPrice().multiply(BigDecimal.valueOf(invoiceItemRequest.getAmount()).multiply(TAX).divide(ONE_HUNDRED)));
        return invoiceItem;
    }

    public static InvoiceItem from(Integer amount, BigDecimal price, String description, BigDecimal tax) {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setPrice(price);
        invoiceItem.setAmount(amount);
        invoiceItem.setDescription(description);
        invoiceItem.setTax(tax);
        return invoiceItem;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
}
