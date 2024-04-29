package com.mstftrgt.ebank.dto.model;

import lombok.Data;


@Data
public class CustomerDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
}
