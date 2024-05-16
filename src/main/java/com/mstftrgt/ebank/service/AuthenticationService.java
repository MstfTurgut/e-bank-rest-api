package com.mstftrgt.ebank.service;


import com.mstftrgt.ebank.dto.model.AddressDto;
import com.mstftrgt.ebank.dto.model.CustomerDto;
import com.mstftrgt.ebank.dto.request.auth.LoginCustomerRequest;
import com.mstftrgt.ebank.dto.request.auth.RegisterCustomerRequest;
import com.mstftrgt.ebank.exception.*;
import com.mstftrgt.ebank.model.Address;
import com.mstftrgt.ebank.model.City;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.model.District;
import com.mstftrgt.ebank.repository.AddressRepository;
import com.mstftrgt.ebank.repository.CityRepository;
import com.mstftrgt.ebank.repository.CustomerRepository;
import com.mstftrgt.ebank.repository.DistrictRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    public AuthenticationService(CustomerRepository customerRepository, AddressRepository addressRepository, CityRepository cityRepository, DistrictRepository districtRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
    }

    public CustomerDto register(RegisterCustomerRequest registerRequest) {

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(registerRequest.getEmail());

        if (optionalCustomer.isPresent()) throw new EmailAlreadyInUseException("This email already in use.");

        Customer customer = Customer.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .dateOfBirth(registerRequest.getDateOfBirth())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .build();

        Optional<City> city = cityRepository.findByTitle(registerRequest.getCity());

        if(city.isEmpty()) throw new CityNotFoundException("City not found.");

        Optional<District> district = districtRepository
                .findByTitleAndCityId(registerRequest.getDistrict(), city.get().getId());

        if(district.isEmpty())
            throw new DistrictNotFoundException("District " + registerRequest.getDistrict() +  " not found for the city : " + city.get().getTitle());

        Customer savedCustomer = customerRepository.save(customer);

        Address address = Address.builder()
                .plainAddress(registerRequest.getPlainAddress())
                .city(city.get())
                .district(district.get())
                .customerId(savedCustomer.getId())
                .build();

        Address savedAddress = addressRepository.save(address);

        CustomerDto customerDto = modelMapper.map(savedCustomer, CustomerDto.class);

        customerDto.setAddress(modelMapper.map(savedAddress, AddressDto.class));

        return customerDto;
    }

    public Customer login(LoginCustomerRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        return customerRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
    }

}
