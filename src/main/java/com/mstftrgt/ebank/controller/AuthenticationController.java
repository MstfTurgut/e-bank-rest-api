package com.mstftrgt.ebank.controller;

import com.mstftrgt.ebank.dto.model.CustomerDto;
import com.mstftrgt.ebank.dto.request.auth.LoginCustomerRequest;
import com.mstftrgt.ebank.dto.response.LoginResponse;
import com.mstftrgt.ebank.dto.request.auth.RegisterCustomerRequest;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.service.AuthenticationService;
import com.mstftrgt.ebank.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;


    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerDto> register(@RequestBody @Valid RegisterCustomerRequest registerCustomerDto) {
        CustomerDto registeredCustomer = authenticationService.register(registerCustomerDto);
        return ResponseEntity.ok(registeredCustomer);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginCustomerRequest loginCustomerDto) {
        Customer authenticatedCustomer = authenticationService.login(loginCustomerDto);
        String jwtToken = jwtService.generateToken(authenticatedCustomer);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }
}
