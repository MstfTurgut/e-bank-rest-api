package com.mstftrgt.ebank.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class LoginCustomerDto {
    private String email;
    private String password;
}
