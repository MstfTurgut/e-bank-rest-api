package com.mstftrgt.ebank.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AccountDto {
    private String id;
    private String customerId;
    private String accountNumber;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}
