package com.mstftrgt.ebank.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class NewMoneyTransferRequestDto {

    @Size(min = 12, max = 12, message = "Account number must has length of twelve.")
    private String receiverAccountNumber;

    @DecimalMin(value = "0.0", message = "Transfer amount cannot be less than zero.")
    @DecimalMax(value = "200000000.0", message = "Transfer amount cannot be more than two hundred million.")
    private BigDecimal amount;

    private String description;
}
