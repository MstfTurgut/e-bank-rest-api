package com.mstftrgt.ebank.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class CustomerDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
}
