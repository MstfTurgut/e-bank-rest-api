package com.mstftrgt.ebank.dto.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
