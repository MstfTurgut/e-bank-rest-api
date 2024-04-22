package com.mstftrgt.ebank.controller;

import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/e-bank/v1/customers")
public class CustomerController {
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String id) {

        Optional<Customer> customer = customerRepository.findById(id);

        if(customer.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(customer.get());
    }

    @PostMapping
    public ResponseEntity<Void> addCustomer(@RequestBody Customer customer, UriComponentsBuilder ucb) {

        Customer savedCustomer = customerRepository.save(customer);

        URI locationOfNewUser = ucb
                .path("e-bank/v1/customers/{id}")
                .buildAndExpand(savedCustomer.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewUser).build();

    }

}
