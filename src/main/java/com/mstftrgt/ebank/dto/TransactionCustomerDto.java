package com.mstftrgt.ebank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionCustomerDto {
    private String id;
    private String firstName;
    private String lastName;
}
