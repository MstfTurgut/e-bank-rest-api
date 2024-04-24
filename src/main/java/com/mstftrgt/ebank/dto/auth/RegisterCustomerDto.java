package com.mstftrgt.ebank.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RegisterCustomerDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private LocalDateTime dateOfBirth;
}
