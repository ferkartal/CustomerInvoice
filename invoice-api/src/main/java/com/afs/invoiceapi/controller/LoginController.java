package com.afs.invoiceapi.controller;

import com.afs.invoiceapi.service.CustomerService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping
@CrossOrigin(origins = {"${accepted.url}"})
public class LoginController {

    private final CustomerService customerService;

    public LoginController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(path = "/auth")
    public String user(Principal user) {
        return customerService.retrieveCustomerId(user.getName());
    }
}
