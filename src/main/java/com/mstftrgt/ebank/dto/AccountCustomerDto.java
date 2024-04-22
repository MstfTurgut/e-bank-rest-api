package com.mstftrgt.ebank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCustomerDto {
    private String id;
    private String email;
    private CustomerDetailsDto customerDetails;
}
