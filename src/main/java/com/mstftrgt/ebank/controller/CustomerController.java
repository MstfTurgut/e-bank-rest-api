package com.mstftrgt.ebank.controller;

import com.mstftrgt.ebank.dto.model.CustomerDto;
import com.mstftrgt.ebank.model.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final ModelMapper modelMapper;
    public CustomerController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerDto> getCustomer() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Customer currentCustomer = (Customer) authentication.getPrincipal();

        CustomerDto customerDto = modelMapper.map(currentCustomer, CustomerDto.class);

        return ResponseEntity.ok(customerDto);
    }

}
