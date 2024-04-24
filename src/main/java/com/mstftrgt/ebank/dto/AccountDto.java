package com.mstftrgt.ebank.dto;
import com.mstftrgt.ebank.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String id;
    private CustomerDto customerDto;
    private String accountNumber;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}
