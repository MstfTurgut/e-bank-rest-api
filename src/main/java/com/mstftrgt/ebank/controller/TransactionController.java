package com.mstftrgt.ebank.controller;


import com.mstftrgt.ebank.dto.model.TransactionDto;
import com.mstftrgt.ebank.dto.request.NewMoneyTransferRequest;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("accounts/{id}/transfer-money")
    public ResponseEntity<Void> transferMoney(@RequestBody @Valid NewMoneyTransferRequest transferRequestDto,
                                              @PathVariable String id) {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        transactionService.transferMoney(transferRequestDto, id, customer.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("accounts/{id}/transaction-history")
    public ResponseEntity<List<TransactionDto>> getTransactionHistory(@PathVariable String id, Pageable pageable) {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(transactionService.getTransactionHistory(id, customer.getId(), pageable));
    }

}
