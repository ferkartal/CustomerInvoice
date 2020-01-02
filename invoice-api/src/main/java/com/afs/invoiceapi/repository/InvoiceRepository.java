package com.afs.invoiceapi.repository;

import com.afs.invoiceapi.exceptions.DomainNotFoundException;
import com.afs.invoiceapi.model.Invoice;
import com.afs.invoiceapi.model.InvoiceItem;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class InvoiceRepository {

    private static final String CREATE_INVOICE_QUERY = "insert into invoice (id, customer_id, created_date, total_price) values(?, ?, ?, ?)";

    private static final String CREATE_INVOICE_ITEM_QUERY = "insert into invoice_item (invoice_id, description, amount, price, tax) values(?, ?, ?, ?, ?)";

    private static final String DELETE_INVOICE_QUERY = "delete from invoice where id=? and customer_id=?";

    private static final String DELETE_INVOICES_QUERY = "delete  from invoice where customer_id=?";

    private static final String DELETE_INVOICE_ITEMS_QUERY = "delete from invoice_item where invoice_id=?";

    private static final String UPDATE_INVOICE_QUERY = "update invoice set created_date = ?, total_price =? where id=? and customer_id=?";

    private static final String RETRIEVE_INVOICE_QUERY = "select * from invoice where id=? and customer_id=?";

    private static final String RETRIEVE_INVOICES_QUERY = "select * from invoice where customer_id=?";

    private static final String RETRIEVE_INVOICE_ITEMS_QUERY = "select * from invoice_item where invoice_id=?";

    private final JdbcTemplate jdbcTemplate;

    public InvoiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createInvoice(Invoice invoice) {

        jdbcTemplate.update(CREATE_INVOICE_QUERY, invoice.getId(), invoice.getCustomerId(),
                invoice.getCreatedDate(), invoice.getTotalPrice());

        List<Object[]> batch = invoice.getInvoiceItems().stream().map(invoiceItem -> new Object[]{
                invoice.getId(), invoiceItem.getDescription(),
                invoiceItem.getAmount(), invoiceItem.getPrice(), invoiceItem.getTax()
        }).collect(Collectors.toList());

        jdbcTemplate.batchUpdate(CREATE_INVOICE_ITEM_QUERY, batch);

    }

    public void updateInvoice(Invoice invoice) {

        int rowCount = jdbcTemplate.update(UPDATE_INVOICE_QUERY, invoice.getCreatedDate(), invoice.getTotalPrice(),
                invoice.getId(), invoice.getCustomerId());

        if (rowCount != 0) {
            jdbcTemplate.update(DELETE_INVOICE_ITEMS_QUERY, invoice.getId());

            List<Object[]> batch = invoice.getInvoiceItems().stream().map(invoiceItem -> new Object[]{
                    invoice.getId(), invoiceItem.getDescription(),
                    invoiceItem.getAmount(), invoiceItem.getPrice(), invoiceItem.getTax()
            }).filter(Objects::nonNull).collect(Collectors.toList());

            jdbcTemplate.batchUpdate(CREATE_INVOICE_ITEM_QUERY, batch);
        } else {
            throw new DomainNotFoundException("invoice.not.found", invoice.getId(), invoice.getCustomerId());
        }
    }

    public void deleteInvoice(String invoiceId, String customerId) {

        jdbcTemplate.update(DELETE_INVOICE_QUERY, invoiceId, customerId);

        jdbcTemplate.update(DELETE_INVOICE_ITEMS_QUERY, invoiceId);
    }

    public void deleteInvoices(String customerId) {

        List<Object[]> invoiceIds = jdbcTemplate.query(RETRIEVE_INVOICES_QUERY,
                new Object[]{customerId}, (rs, rowNum) -> new Object[]{rs.getString("id")});
        jdbcTemplate.update(DELETE_INVOICES_QUERY, customerId);

        jdbcTemplate.batchUpdate(DELETE_INVOICE_ITEMS_QUERY, invoiceIds);
    }

    public List<Invoice> retrieveInvoices(String customerId) {

        List<Invoice> invoices = jdbcTemplate.query(RETRIEVE_INVOICES_QUERY,
                new Object[]{customerId}, (rs, rowNum) -> Invoice.from(rs.getString("id"),
                        rs.getString("customer_id"), rs.getDate("created_date"),
                        rs.getBigDecimal("total_price")));

        invoices.forEach(invoice -> invoice.setInvoiceItems(jdbcTemplate.query(RETRIEVE_INVOICE_ITEMS_QUERY,
                new Object[]{invoice.getId()}, (rs, rowNum) -> InvoiceItem.from(
                        rs.getInt("amount"), rs.getBigDecimal("price"),
                        rs.getString("description"), rs.getBigDecimal("tax")))));

        return invoices;
    }

    public Invoice retrieveInvoice(String invoiceId, String customerId) {

        try {
            Invoice invoice = jdbcTemplate.queryForObject(RETRIEVE_INVOICE_QUERY,
                    new Object[]{invoiceId, customerId}, (rs, rowNum) -> Invoice.from(rs.getString("id"),
                            rs.getString("customer_id"), rs.getDate("created_date"),
                            rs.getBigDecimal("total_price")));

            invoice.setInvoiceItems(jdbcTemplate.query(RETRIEVE_INVOICE_ITEMS_QUERY,
                    new Object[]{invoiceId}, (rs, rowNum) -> InvoiceItem.from(
                            rs.getInt("amount"), rs.getBigDecimal("price"),
                            rs.getString("description"), rs.getBigDecimal("tax"))));
            return invoice;
        } catch (EmptyResultDataAccessException e) {
            throw new DomainNotFoundException("invoice.not.found", invoiceId, customerId);
        }
    }
}
