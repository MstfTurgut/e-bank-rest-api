package com.mstftrgt.ebank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailsDto {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private LocalDateTime dateOfBirth;
}
