package com.afs.invoiceapi.model.response;

import com.afs.invoiceapi.model.Invoice;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceResponse {

    private String id;
    private Date createdDate;
    private List<InvoiceItemResponse> invoiceItems;
    private BigDecimal totalPrice;

    public static InvoiceResponse from(Invoice invoice) {
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setCreatedDate(invoice.getCreatedDate());
        invoiceResponse.setId(invoice.getId());
        invoiceResponse.setInvoiceItems(invoice.getInvoiceItems()
                .stream()
                .map(InvoiceItemResponse::from)
                .collect(Collectors.toList()));
        invoiceResponse.setTotalPrice(invoice.getTotalPrice());
        return invoiceResponse;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<InvoiceItemResponse> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItemResponse> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
