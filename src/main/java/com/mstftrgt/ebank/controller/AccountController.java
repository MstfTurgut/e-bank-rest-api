package com.mstftrgt.ebank.controller;


import com.mstftrgt.ebank.model.Account;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.repository.AccountRepository;
import com.mstftrgt.ebank.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/e-bank/v1/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;

    public AccountController(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable String id) {

        Optional<Account> byId = accountRepository.findById(id);

        if(byId.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(byId.get());
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok(accountRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> addAccount(@RequestBody Account account, UriComponentsBuilder ucb) {

        Optional<Customer> byId = customerRepository.findById(account.getCustomer().getId());

        account.setCustomer(byId.get());

        Account savedAccount = accountRepository.save(account);

        URI location = ucb.path("e-bank/v1/accounts/{id}").buildAndExpand(savedAccount.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

}
