package com.mstftrgt.ebank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewAccountRequestDto {
    private BigDecimal initialBalance;
}
