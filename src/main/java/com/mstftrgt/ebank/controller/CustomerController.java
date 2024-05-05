package com.mstftrgt.ebank.controller;

import com.mstftrgt.ebank.dto.model.CustomerDto;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerDto> getCurrentCustomer() {

        return ResponseEntity.ok(customerService.getCurrentCustomer());
    }

}
