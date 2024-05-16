package com.mstftrgt.ebank.dto.model;

import com.mstftrgt.ebank.model.Transaction;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionDto {
    @Enumerated(EnumType.STRING)
    private Transaction.TransactionType transactionType;
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
    private TransactionCustomerDto senderCustomer;
    private TransactionCustomerDto receiverCustomer;
}
