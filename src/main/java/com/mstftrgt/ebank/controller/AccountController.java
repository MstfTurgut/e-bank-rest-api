package com.mstftrgt.ebank.controller;

import com.mstftrgt.ebank.dto.model.AccountDto;
import com.mstftrgt.ebank.dto.request.NewAccountRequestDto;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable String id) {

        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AccountDto accountDto = accountService.getAccountById(id, customer.getId());

        return ResponseEntity.ok(accountDto);
    }

    @PostMapping("/create-account")
    public ResponseEntity<Void> createAccount(@RequestBody @Valid NewAccountRequestDto newAccountRequest, UriComponentsBuilder ucb) {

        AccountDto  accountDto = accountService.createNewAccount(newAccountRequest);

        URI locationOfNewAccount = ucb.path("accounts/{id}").buildAndExpand(accountDto.getId()).toUri();

        return ResponseEntity.created(locationOfNewAccount).build();
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> myAccounts() {

        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(accountService.getAllAccounts(customer));

    }

    @DeleteMapping("/{id}/delete-account")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {

        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        accountService.deleteAccountById(id, customer.getId());
        return ResponseEntity.noContent().build();
    }

}
