package com.mstftrgt.ebank.controller;

import com.mstftrgt.ebank.dto.model.CustomerDto;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;
    public CustomerController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerDto> getCustomer() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Customer currentCustomer = (Customer) authentication.getPrincipal();

        CustomerDto customerDto = modelMapper.map(currentCustomer, CustomerDto.class);

        return ResponseEntity.ok(customerDto);
    }

    @PostMapping("/")
    public ResponseEntity<List<CustomerDto>> allCustomers() {
        List<CustomerDto> customers = customerService.allCustomers();
        return ResponseEntity.ok(customers);
    }

}
