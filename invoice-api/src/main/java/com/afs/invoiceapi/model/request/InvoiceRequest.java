package com.afs.invoiceapi.model.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class InvoiceRequest {

    private String createdDate;
    @Valid
    @NotNull(message = "invoiceItems.cannot.be.null")
    @Size(min = 1, message = "invoiceItem.count.min")
    private List<InvoiceItemRequest> invoiceItems;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<InvoiceItemRequest> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItemRequest> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }
}
