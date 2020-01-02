package com.afs.invoiceapi.controller;

import com.afs.invoiceapi.controller.exception.ErrorResponse;
import com.afs.invoiceapi.model.Customer;
import com.afs.invoiceapi.model.Invoice;
import com.afs.invoiceapi.model.InvoiceItem;
import com.afs.invoiceapi.model.request.CustomerRequest;
import com.afs.invoiceapi.model.request.InvoiceItemRequest;
import com.afs.invoiceapi.model.request.InvoiceRequest;
import com.afs.invoiceapi.model.request.UpdateCustomerRequest;
import com.afs.invoiceapi.service.CustomerService;
import com.afs.invoiceapi.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    @MockBean
    InvoiceService invoiceService;

    @Test
    public void it_should_create_customer() throws Exception {
        //given
        String password = "123";
        String address = "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522";
        String birthDate = "1992-09-11";
        String email = "email@email.com";
        String firstName = "Elon";
        String lastName = "Musk";
        String phoneNumber = "9999999998";

        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setPassword(password);
        customerRequest.setAddress(address);
        customerRequest.setBirthDate(birthDate);
        customerRequest.setEmail(email);
        customerRequest.setFirstName(firstName);
        customerRequest.setLastName(lastName);
        customerRequest.setPhoneNumber(phoneNumber);

        Mockito.doNothing().when(customerService).createCustomer(Mockito.any(Customer.class));
        Mockito.when(customerService.retrieveCustomer(Mockito.anyString())).thenReturn(new Customer());

        //when
        ResultActions resultActions = mockMvc.perform(post("/customers")
                .content(objectMapper.writeValueAsString(customerRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void it_should_throw_validation_exception_when_creating_customer_with_missing_mandatory_fields() throws Exception {
        //given
        CustomerRequest customerRequest = new CustomerRequest();

        //when
        ResultActions resultActions = mockMvc.perform(post("/customers")
                .content(objectMapper.writeValueAsString(customerRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isBadRequest());
        ErrorResponse errorResponse = new ObjectMapper().readValue(resultActions.andReturn().getResponse().getContentAsString(),
                ErrorResponse.class);

        Assertions.assertThat(errorResponse.getErrors()).hasSize(7).extracting(
                "message"
        ).containsExactlyInAnyOrder(
                "Please enter your lastName.", "Please enter your address.", "Please enter your email address.",
                "Please enter firstName.", "Please enter your phoneNumber.", "Please enter your password.",
                "Please enter your birth date.");

    }

    @Test
    public void it_should_throw_validation_exception_when_creating_customer_with_invalid_regex() throws Exception {
        //given
        String password = "123";
        String address = "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522";
        String birthDate = "1992-09-11";
        String email = "email";
        String firstName = "Elon";
        String lastName = "Musk";
        String phoneNumber = "9999999998+89";

        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setPassword(password);
        customerRequest.setAddress(address);
        customerRequest.setBirthDate(birthDate);
        customerRequest.setEmail(email);
        customerRequest.setFirstName(firstName);
        customerRequest.setLastName(lastName);
        customerRequest.setPhoneNumber(phoneNumber);

        //when
        ResultActions resultActions = mockMvc.perform(post("/customers")
                .content(objectMapper.writeValueAsString(customerRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isBadRequest());
        ErrorResponse errorResponse = new ObjectMapper().readValue(resultActions.andReturn().getResponse().getContentAsString(),
                ErrorResponse.class);
        Assertions.assertThat(errorResponse.getErrors()).hasSize(2).extracting(
                "message"
        ).containsExactlyInAnyOrder(
                "Please enter the email in format : yourname@example.com", "Please enter the phone number in format: 3334241234");

    }

    @Test
    @WithMockUser(username = "email@email.com", authorities = "ROLE_ADMIN")
    public void it_should_update_customer() throws Exception {
        //given
        String updatedAddress = "Cecilia Chapman 711-2880 Nulla Mississippi 96522";
        String firstName = "Elon";
        String lastName = "Musk";
        String customerId = "1";

        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest();
        updateCustomerRequest.setAddress(updatedAddress);
        updateCustomerRequest.setFirstName(firstName);
        updateCustomerRequest.setLastName(lastName);

        Mockito.doNothing().when(customerService).updateCustomer(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        Mockito.when(customerService.retrieveCustomer(Mockito.anyString())).thenReturn(new Customer());

        //when
        ResultActions resultActions = mockMvc.perform(put("/customers/" + customerId)
                .content(objectMapper.writeValueAsString(updateCustomerRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "email@email.com", authorities = "ROLE_ADMIN")
    public void it_should_throw_validation_exception_when_updating_customer_with_missing_mandatory_fields() throws Exception {
        //given
        String updatedAddress = "Cecilia Chapman 711-2880 Nulla Mississippi 96522";
        String firstName = "Elon";
        String customerId = "1";

        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest();
        updateCustomerRequest.setAddress(updatedAddress);
        updateCustomerRequest.setFirstName(firstName);

        //when
        ResultActions resultActions = mockMvc.perform(put("/customers/" + customerId)
                .content(objectMapper.writeValueAsString(updateCustomerRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isBadRequest());
        ErrorResponse errorResponse = new ObjectMapper().readValue(resultActions.andReturn().getResponse().getContentAsString(),
                ErrorResponse.class);
        assertEquals(1, errorResponse.getErrors().size());

        assertEquals("Please enter your lastName.", errorResponse.getErrors().get(0).getMessage());
    }

    @Test
    @WithMockUser(username = "email@email.com", authorities = "ROLE_ADMIN")
    public void it_should_delete_customer() throws Exception {
        //given
        String customerId = "1";

        Mockito.doNothing().when(customerService).deleteCustomer(Mockito.anyString());

        //when
        ResultActions resultActions = mockMvc.perform(delete("/customers/" + customerId)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());

    }

    @Test
    @WithMockUser(username = "email@email.com", authorities = "ROLE_ADMIN")
    public void it_should_retrieve_customer() throws Exception {
        //given
        String customerId = "1";

        Mockito.when(customerService.retrieveCustomer(Mockito.anyString())).thenReturn(new Customer());

        //when
        ResultActions resultActions = mockMvc.perform(get("/customers/" + customerId)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "email@email.com", authorities = "ROLE_ADMIN")
    public void it_should_create_invoice() throws Exception {

        //given
        String customerId = "1";
        String createdDate = "2019-01-01";
        Integer amount = 99;
        String description = "invoice description";
        BigDecimal price = new
                BigDecimal("123.09");

        InvoiceItemRequest invoiceItemRequest = new InvoiceItemRequest();
        invoiceItemRequest.setAmount(amount);
        invoiceItemRequest.setDescription(description);
        invoiceItemRequest.setPrice(price);

        List<InvoiceItemRequest> invoiceItems = new ArrayList<>();
        invoiceItems.add(invoiceItemRequest);

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setCreatedDate(createdDate);
        invoiceRequest.setInvoiceItems(invoiceItems);

        List<InvoiceItem> invoiceItem = new ArrayList<>();
        Invoice invoice = new Invoice();
        invoice.setInvoiceItems(invoiceItem);

        Mockito.doNothing().when(invoiceService).createInvoice(Mockito.any(Invoice.class));
        Mockito.when(invoiceService.retrieveInvoice(Mockito.anyString(), Mockito.anyString())).thenReturn(invoice);

        //when
        ResultActions resultActions = mockMvc.perform(post("/customers/" + customerId + "/invoices")
                .content(objectMapper.writeValueAsString(invoiceRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "email@email.com", authorities = "ROLE_ADMIN")
    public void it_should_throw_validation_exception_when_creating_invoice_with_missing_invoice_items() throws Exception {

        //given
        String customerId = "1";

        InvoiceRequest invoiceRequest = new InvoiceRequest();

        //when
        ResultActions resultActions = mockMvc.perform(post("/customers/" + customerId + "/invoices")
                .content(objectMapper.writeValueAsString(invoiceRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isBadRequest());

        ErrorResponse errorResponse = new ObjectMapper().readValue(resultActions.andReturn().getResponse().getContentAsString(),
                ErrorResponse.class);

        assertEquals(1, errorResponse.getErrors().size());
        assertEquals("Please enter the invoiceItems.", errorResponse.getErrors().get(0).getMessage());

    }

    @Test
    @WithMockUser(username = "email@email.com", authorities = "ROLE_ADMIN")
    public void it_should_throw_validation_exception_when_creating_invoice_with_missing_mandatory_fields() throws Exception {

        //given
        String customerId = "1";
        Integer amount = 99;
        String description = "invoice description";
        BigDecimal price = new
                BigDecimal("123.09");

        InvoiceItemRequest invoiceItemRequest = new InvoiceItemRequest();
        invoiceItemRequest.setAmount(amount);
        invoiceItemRequest.setDescription(description);
        invoiceItemRequest.setPrice(price);

        List<InvoiceItemRequest> invoiceItems = new ArrayList<>();
        invoiceItems.add(invoiceItemRequest);
        invoiceItems.add(new InvoiceItemRequest());

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setInvoiceItems(invoiceItems);

        //when
        ResultActions resultActions = mockMvc.perform(post("/customers/" + customerId + "/invoices")
                .content(objectMapper.writeValueAsString(invoiceRequest))
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest());
        ErrorResponse errorResponse = new ObjectMapper().readValue(resultActions.andReturn().getResponse().getContentAsString(),
                ErrorResponse.class);

        Assertions.assertThat(errorResponse.getErrors()).hasSize(3).extracting(
                "message"
        ).containsExactlyInAnyOrder(
                "Please enter the price.", "Please enter the description.", "Please enter the amount.");


    }

    @Test
    @WithMockUser(username = "email@email.com", authorities = "ROLE_ADMIN")
    public void it_should_update_invoice() throws Exception {

        //given
        String customerId = "1";
        String invoiceId = "2";
        String createdDate = "2019-01-01";
        Integer amount = 90;
        String description = "invoice description";
        BigDecimal price = new
                BigDecimal("123.89");

        InvoiceItemRequest updateInvoiceItemRequest = new InvoiceItemRequest();
        updateInvoiceItemRequest.setAmount(amount);
        updateInvoiceItemRequest.setDescription(description);
        updateInvoiceItemRequest.setPrice(price);

        List<InvoiceItemRequest> invoiceItems = new ArrayList<>();
        invoiceItems.add(updateInvoiceItemRequest);

        InvoiceRequest updateInvoiceRequest = new InvoiceRequest();
        updateInvoiceRequest.setCreatedDate(createdDate);
        updateInvoiceRequest.setInvoiceItems(invoiceItems);

        List<InvoiceItem> updateInvoiceItem = new ArrayList<>();
        Invoice invoice = new Invoice();
        invoice.setInvoiceItems(updateInvoiceItem);

        Mockito.doNothing().when(invoiceService).updateInvoice(Mockito.any(Invoice.class));
        Mockito.when(invoiceService.retrieveInvoice(Mockito.anyString(), Mockito.anyString())).thenReturn(invoice);

        //when
        ResultActions resultActions = mockMvc.perform(put("/customers/" + customerId + "/invoices/" + invoiceId)
                .content(objectMapper.writeValueAsString(updateInvoiceRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk());

    }


    @Test
    @WithMockUser(username = "email@email.com", authorities = "ROLE_ADMIN")
    public void it_should_delete_invoice() throws Exception {

        //given
        String customerId = "1";
        String invoiceId = "2";

        Mockito.doNothing().when(invoiceService).deleteInvoice(Mockito.anyString(), Mockito.anyString());

        //when
        ResultActions resultActions = mockMvc.perform(delete("/customers/" + customerId + "/invoices/" + invoiceId)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isNoContent());

    }

    @Test
    @WithMockUser(username = "email@email.com", authorities = "ROLE_ADMIN")
    public void it_should_retrieve_invoice() throws Exception {

        //given
        String customerId = "1";

        List<InvoiceItem> invoiceItem = new ArrayList<>();
        Invoice invoice = new Invoice();
        invoice.setInvoiceItems(invoiceItem);

        Mockito.when(invoiceService.retrieveInvoice(Mockito.anyString(), Mockito.anyString())).thenReturn(invoice);

        //when
        ResultActions resultActions = mockMvc.perform(get("/customers/" + customerId + "/invoices/")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk());

    }

}