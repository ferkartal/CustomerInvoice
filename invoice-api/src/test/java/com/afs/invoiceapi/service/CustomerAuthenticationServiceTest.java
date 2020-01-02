package com.afs.invoiceapi.service;

import com.afs.invoiceapi.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerAuthenticationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void it_should_retrieve_user() {

        //given
        String email = "email@email.com";
        String password = "password";
        User user = new User(email, password,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        Mockito.when(customerRepository.retrieveUser(email)).thenReturn(user);

        //when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        //then
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertEquals(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")), userDetails.getAuthorities());

    }

    @Test
    public void it_should_throw_exception_when_user_does_not_exist() {

        //given
        String email = "email@email.com";
        Mockito.doThrow(EmptyResultDataAccessException.class).when(customerRepository).retrieveUser(email);

        //when
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(email));

        //then
        assertEquals("Username[" + email + "] not found", exception.getMessage());
    }

}