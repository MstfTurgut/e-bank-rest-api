package com.mstftrgt.ebank.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class CustomerDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    private AddressDto address;
}
