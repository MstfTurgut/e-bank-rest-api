package com.mstftrgt.ebank.service;


import com.mstftrgt.ebank.dto.model.AddressDto;
import com.mstftrgt.ebank.dto.model.CustomerDto;
import com.mstftrgt.ebank.dto.request.auth.LoginCustomerRequestDto;
import com.mstftrgt.ebank.dto.request.auth.RegisterCustomerRequestDto;
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

    public CustomerDto register(RegisterCustomerRequestDto registerCustomerDto) {

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(registerCustomerDto.getEmail());

        if (optionalCustomer.isPresent()) throw new EmailAlreadyInUseException("This email already in use.");

        Customer customer = Customer.builder()
                .firstName(registerCustomerDto.getFirstName())
                .lastName(registerCustomerDto.getLastName())
                .dateOfBirth(registerCustomerDto.getDateOfBirth())
                .email(registerCustomerDto.getEmail())
                .password(passwordEncoder.encode(registerCustomerDto.getPassword()))
                .phoneNumber(registerCustomerDto.getPhoneNumber())
                .build();

        Optional<City> city = cityRepository.findByTitle(registerCustomerDto.getCity());

        if(city.isEmpty()) throw new CityNotFoundException("City not found.");

        Optional<District> district = districtRepository
                .findByTitleAndCityId(registerCustomerDto.getDistrict(), city.get().getId());

        if(district.isEmpty())
            throw new DistrictNotFoundException("District " + registerCustomerDto.getDistrict() +  " not found for the city : " + city.get().getTitle());

        Customer savedCustomer = customerRepository.save(customer);

        Address address = Address.builder()
                .plainAddress(registerCustomerDto.getPlainAddress())
                .city(city.get())
                .district(district.get())
                .customerId(savedCustomer.getId())
                .build();

        Address savedAddress = addressRepository.save(address);

        CustomerDto customerDto = modelMapper.map(savedCustomer, CustomerDto.class);

        customerDto.setAddress(modelMapper.map(savedAddress, AddressDto.class));

        return customerDto;
    }

    public Customer login(LoginCustomerRequestDto loginCustomerDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginCustomerDto.getEmail(), loginCustomerDto.getPassword()));
        return customerRepository.findByEmail(loginCustomerDto.getEmail()).orElseThrow();
    }

}
