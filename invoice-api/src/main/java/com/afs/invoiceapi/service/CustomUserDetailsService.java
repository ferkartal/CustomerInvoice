package com.afs.invoiceapi.service;

import com.afs.invoiceapi.repository.CustomerRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            return customerRepository.retrieveUser(userName);
        } catch (EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException(String.format("Username[%s] not found", userName));
        }
    }
}