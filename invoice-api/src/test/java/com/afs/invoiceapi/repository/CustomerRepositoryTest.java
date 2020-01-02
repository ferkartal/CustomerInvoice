package com.afs.invoiceapi.repository;

import com.afs.invoiceapi.exceptions.DomainNotFoundException;
import com.afs.invoiceapi.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private CustomerRepository customerRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void it_should_create_customer() throws ParseException {
        String encryptedPassword = "abc?";
        Customer customer = generateCustomerData();

        Mockito.when(bCryptPasswordEncoder.encode(customer.getPassword())).thenReturn(encryptedPassword);
        Mockito.when(jdbcTemplate.update(CustomerRepository.CREATE_CUSTOMER_QUERY, customer.getId(), customer.getFirstName(), customer.getLastName(),
                customer.getEmail(), encryptedPassword, customer.getBirthDate(), customer.getPhoneNumber(),
                customer.getAddress())).thenReturn(1);

        //when && then
        assertDoesNotThrow(() -> customerRepository.createCustomer(customer));

    }

    @Test
    public void it_should_retrieve_customer() throws ParseException {
        //given
        Customer customer = generateCustomerData();

        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(),
                Mockito.any(Object[].class), Mockito.any(RowMapper.class))).thenReturn(customer);
        //when
        Customer retrievedCustomer = customerRepository.retrieveCustomer(customer.getId());

        //then
        assertNotNull(retrievedCustomer);
        assertEquals(customer.getId(), retrievedCustomer.getId());
        assertEquals(customer.getPhoneNumber(), retrievedCustomer.getPhoneNumber());
        assertEquals(customer.getLastName(), retrievedCustomer.getLastName());
        assertEquals(customer.getFirstName(), retrievedCustomer.getFirstName());
        assertEquals(customer.getEmail(), retrievedCustomer.getEmail());
        assertEquals(customer.getBirthDate(), retrievedCustomer.getBirthDate());
        assertEquals(customer.getAddress(), retrievedCustomer.getAddress());
        assertEquals(customer.getPassword(), retrievedCustomer.getPassword());

    }

    @Test
    public void it_should_throw_domain_not_found_exception_when_retrieving_customer() {
        //given
        String customerId = "1";
        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(),
                Mockito.any(Object[].class), Mockito.any(RowMapper.class))).thenThrow(new EmptyResultDataAccessException(1));
        //when
        DomainNotFoundException exception = assertThrows(DomainNotFoundException.class, () -> customerRepository.retrieveCustomer(customerId));

        //then
        assertEquals("customer.not.found", exception.getKey());
        assertEquals(customerId, exception.getArgs()[0]);

    }

    @Test
    public void it_should_retrieve_user_by_email() {
        //given
        String email = "email@email.com";
        String password = "123";
        Collection<SimpleGrantedAuthority> roles = Collections.unmodifiableSet(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        User user = new User(email, password, roles);

        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(),
                Mockito.any(Object[].class), Mockito.any(RowMapper.class))).thenReturn(user);
        //when
        User retrievedUser = customerRepository.retrieveUser(email);

        //then
        assertNotNull(retrievedUser);
        assertEquals(email, retrievedUser.getUsername());
        assertEquals(password, retrievedUser.getPassword());
        assertEquals(roles, retrievedUser.getAuthorities());

    }

    @Test
    public void it_should_return_email_is_valid() {
        //given
        String email = "email@email.com";

        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(),
                Mockito.any(Object[].class), Mockito.any(Class.class))).thenReturn(0);

        //when
        boolean isValid = customerRepository.emailIsValid(email);

        //then
        assertTrue(isValid);
    }

    @Test
    public void it_should_return_email_is_invalid() {
        //given
        String email = "email@email.com";

        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(),
                Mockito.any(Object[].class), Mockito.any(Class.class))).thenReturn(1);

        //when
        boolean isValid = customerRepository.emailIsValid(email);

        //the
        assertFalse(isValid);
    }

    private Customer generateCustomerData() throws ParseException {

        String id = "1";
        String address = "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522";
        String email = "email@email.com";
        Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse("1992-09-11");
        String firstName = "Elon";
        String lastName = "Musk";
        String phoneNumber = "2575637401";
        String password = "123";

        Customer customer = new Customer();
        customer.setId(id);
        customer.setAddress(address);
        customer.setEmail(email);
        customer.setBirthDate(birthDate);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhoneNumber(phoneNumber);
        customer.setPassword(password);
        return customer;
    }

}