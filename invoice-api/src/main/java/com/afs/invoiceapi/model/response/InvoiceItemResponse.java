package com.afs.invoiceapi.model.response;

import com.afs.invoiceapi.model.InvoiceItem;

import java.math.BigDecimal;

public class InvoiceItemResponse {

    private Integer amount;
    private BigDecimal price;
    private String description;
    private BigDecimal tax;

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

    public static InvoiceItemResponse from(InvoiceItem invoiceItem) {
        InvoiceItemResponse invoiceItemResponse = new InvoiceItemResponse();
        invoiceItemResponse.setAmount(invoiceItem.getAmount());
        invoiceItemResponse.setDescription(invoiceItem.getDescription());
        invoiceItemResponse.setPrice(invoiceItem.getPrice());
        invoiceItemResponse.setTax(invoiceItem.getTax());
        return invoiceItemResponse;
    }
}
