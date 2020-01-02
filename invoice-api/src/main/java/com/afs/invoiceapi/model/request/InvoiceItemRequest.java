package com.afs.invoiceapi.model.request;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class InvoiceItemRequest {
    @NotNull(message = "amount.cannot.be.null")
    @Min(value = 1, message = "amount.should.be.greater.than")
    private Integer amount;
    @NotNull(message = "price.cannot.be.null")
    @DecimalMin(value = "0.0", inclusive = false, message = "price.should.be.greater.than")
    private BigDecimal price;
    @NotNull(message = "description.cannot.be.null")
    @Size(max = 30, message = "description.max")
    private String description;

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
}
