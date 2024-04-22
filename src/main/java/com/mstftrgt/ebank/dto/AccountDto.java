package com.mstftrgt.ebank.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String id;
    private AccountCustomerDto customer;
    private String accountNumber;
    private BigDecimal balance;
    private LocalDateTime creationDate;
    private Set<CardDto> cards;
    private Set<TransactionDto> transactions;
}
