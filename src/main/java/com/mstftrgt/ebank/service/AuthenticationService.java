package com.mstftrgt.ebank.service;


import com.mstftrgt.ebank.dto.model.CustomerDto;
import com.mstftrgt.ebank.dto.request.auth.LoginCustomerRequestDto;
import com.mstftrgt.ebank.dto.request.auth.RegisterCustomerRequestDto;
import com.mstftrgt.ebank.exception.EmailAlreadyInUseException;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final ModelMapper modelMapper;

    public AuthenticationService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
    }

    public CustomerDto register(RegisterCustomerRequestDto registerCustomerDto) {

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(registerCustomerDto.getEmail());

        if(optionalCustomer.isPresent()) throw new EmailAlreadyInUseException("This email already in use.");

        Customer customer = new Customer();

        customer.setFirstName(registerCustomerDto.getFirstName());
        customer.setLastName(registerCustomerDto.getLastName());
        customer.setPhoneNumber(registerCustomerDto.getPhoneNumber());
        customer.setAddress(registerCustomerDto.getAddress());
        customer.setDateOfBirth(registerCustomerDto.getDateOfBirth());

        customer.setEmail(registerCustomerDto.getEmail());
        customer.setPassword(passwordEncoder.encode(registerCustomerDto.getPassword()));

        return modelMapper.map(customerRepository.save(customer), CustomerDto.class);
    }

    public Customer login(LoginCustomerRequestDto loginCustomerDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginCustomerDto.getEmail(), loginCustomerDto.getPassword()));
        return customerRepository.findByEmail(loginCustomerDto.getEmail()).orElseThrow();
    }

}
