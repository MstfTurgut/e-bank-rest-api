package com.mstftrgt.ebank.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class NewAccountRequest {

    @DecimalMin(value = "0.0", message = "Initial balance must be at least zero.")
    @DecimalMax(value = "200000000.0", message = "Initial balance cannot exceed two hundred million.")
    @NotNull(message = "Please enter an initial balance.")
    private BigDecimal initialBalance;
}
