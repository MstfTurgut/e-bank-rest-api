package com.mstftrgt.ebank.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewAccountRequestDto {

    @DecimalMin(value = "0.0", message = "Initial balance cannot be less than zero.")
    @DecimalMax(value = "200000000.0", message = "Initial balance cannot be more than two hundred million.")
    private BigDecimal initialBalance;
}
