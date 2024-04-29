package com.mstftrgt.ebank.dto.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountDto {
    private String id;
    private CustomerDto customerDto;
    private String accountNumber;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}
