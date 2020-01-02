package com.afs.invoiceapi.model;

import com.afs.invoiceapi.exceptions.ValidationException;
import com.afs.invoiceapi.model.request.InvoiceRequest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Invoice {

    private String id;
    private String customerId;
    private Date createdDate;
    private List<InvoiceItem> invoiceItems;
    private BigDecimal totalPrice;

    public static Invoice from(InvoiceRequest invoiceRequest, String customerId, String invoiceId) {
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setCustomerId(customerId);
        invoice.setCreatedDate(getDate(invoiceRequest.getCreatedDate()));

        invoice.setInvoiceItems(invoiceRequest.getInvoiceItems()
                .stream()
                .map(InvoiceItem::from)
                .collect(Collectors.toList()));
        invoice.setTotalPrice(calculateTotalPrice(invoice.getInvoiceItems()));
        return invoice;
    }

    private static Date getDate(String createdDate) {
        if (createdDate == null) {
            return Calendar.getInstance().getTime();
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(createdDate);
        } catch (ParseException e) {
            throw new ValidationException("invalid.date", createdDate);
        }
    }

    public static Invoice from(String id, String customerId, Date createdDate, BigDecimal totalPrice) {
        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setCustomerId(customerId);
        invoice.setCreatedDate(createdDate);
        invoice.setTotalPrice(totalPrice);
        return invoice;
    }

    private static BigDecimal calculateTotalPrice(List<InvoiceItem> invoiceItems) {
        return invoiceItems
                .stream()
                .map(invoiceItem -> invoiceItem.getPrice().multiply(BigDecimal.valueOf(invoiceItem.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
