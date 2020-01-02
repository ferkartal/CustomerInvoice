package com.afs.invoiceapi.repository;

import com.afs.invoiceapi.exceptions.DomainNotFoundException;
import com.afs.invoiceapi.model.Customer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class CustomerRepository {

    public static final String CREATE_CUSTOMER_QUERY = "insert into customer (id, first_name, last_name, email, password, birth_date, phone_number, address) " + "values(?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String RETRIEVE_CUSTOMER_QUERY = "select * from customer where id=?";

    public static final String RETRIEVE_CUSTOMER_BY_EMAIL = "select * from customer where email=?";

    public static final String CUSTOMER_COUNT_BY_EMAIL = "select count(*) from customer where email=?";

    public static final String UPDATE_CUSTOMER_QUERY = "update customer set first_name = ?, last_name = ?, address = ? where id=?";

    public static final String DELETE_CUSTOMER_QUERY = "delete from customer where id=?";

    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomerRepository(JdbcTemplate jdbcTemplate, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void createCustomer(Customer customer) {
        String encryptedPassword = bCryptPasswordEncoder.encode(customer.getPassword());
        jdbcTemplate.update(CREATE_CUSTOMER_QUERY,
                customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getEmail(),
                encryptedPassword, customer.getBirthDate(), customer.getPhoneNumber(), customer.getAddress());

    }

    public Customer retrieveCustomer(String customerId) {

        try {
            Customer customer = jdbcTemplate.queryForObject(RETRIEVE_CUSTOMER_QUERY,
                    new Object[]{customerId}, (rs, rowNum) -> Customer.from(
                            rs.getString("id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("email"),
                            rs.getDate("birth_date"), rs.getString("phone_number"),
                            rs.getString("address")));
            return customer;

        } catch (EmptyResultDataAccessException e) {
            throw new DomainNotFoundException("customer.not.found", customerId);
        }
    }

    public void updateCustomer(String firstName, String lastName, String address, String customerId) {
        jdbcTemplate.update(UPDATE_CUSTOMER_QUERY, firstName, lastName,
                address, customerId);
    }

    public void deleteCustomer(String customerId) {
        jdbcTemplate.update(DELETE_CUSTOMER_QUERY, customerId);
    }

    public boolean emailIsValid(String email) {
        int rowCount = jdbcTemplate.queryForObject(CUSTOMER_COUNT_BY_EMAIL,
                new Object[]{email}, Integer.class);

        return rowCount == 0;
    }

    public User retrieveUser(String email) {
        return jdbcTemplate.queryForObject(RETRIEVE_CUSTOMER_BY_EMAIL,
                new Object[]{email}, (rs, rowNum) -> new User(email, rs.getString("password"),
                        Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
    }

    public String retrieveCustomerId(String email) {
        return jdbcTemplate.queryForObject(RETRIEVE_CUSTOMER_BY_EMAIL,
                new Object[]{email}, (rs, rowNum) -> rs.getString("id"));
    }
}
