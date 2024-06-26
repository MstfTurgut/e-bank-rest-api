package com.mstftrgt.ebank.dto.request.auth;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class RegisterCustomerRequest {

    @Email(message = "Please enter a valid email address.")
    @NotBlank(message = "Email field cannot be empty.")
    private String email;

    @NotBlank(message = "Password field cannot be empty.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @NotBlank(message = "First name field cannot be empty.")
    private String firstName;

    @NotBlank(message = "Last name field cannot be empty.")
    private String lastName;

    @Pattern(regexp = "^\\d{10,13}$", message = "Phone number must be between 10 and 13 digits.")
    private String phoneNumber;

    @NotBlank(message = "Address field cannot be empty.")
    @Size(max = 250, message = "Address field cannot be longer than 250 characters.")
    private String plainAddress;

    @NotBlank(message = "City field cannot be empty.")
    private String city;

    @NotBlank(message = "District field cannot be empty.")
    private String district;

    @Past(message = "Date of birth cannot be in the future.")
    private LocalDateTime dateOfBirth;
}
