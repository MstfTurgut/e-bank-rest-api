package com.mstftrgt.ebank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private String id;
    private String email;
    private Set<CustomerAccountDto> accounts;
    private CustomerDetailsDto customerDetails;
}
