package com.javastart.customerservice.customer.controller;

import com.javastart.customerservice.customer.controller.dto.CustomerRequestDTO;
import com.javastart.customerservice.customer.controller.dto.CustomerResponseDTO;
import com.javastart.customerservice.customer.service.CustomerService;
import com.javastart.customerservice.exceptions.UnableValueForRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("")
    public CustomerResponseDTO getCustomer(@RequestParam(value = "accountId", required = false) Long accountId,
                                           @RequestParam(value = "email", required = false) String email) {
        if ((email == null || email.isEmpty()) && accountId == null) {
            throw new UnableValueForRequestException("Unable to find customer, because no data provided");
        }
        if (email != null && !email.isEmpty()) {
            return customerService.getCustomerByEmail(email);
        } else {
            return customerService.getCustomerByAccountId(accountId);
        }

    }

    @PostMapping("/")
    public Object createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO) {
        return customerService.createCustomer(customerRequestDTO);
    }
}
