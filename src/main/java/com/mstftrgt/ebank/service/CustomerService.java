package com.mstftrgt.ebank.service;

import com.mstftrgt.ebank.dto.model.AddressDto;
import com.mstftrgt.ebank.dto.model.CustomerDto;
import com.mstftrgt.ebank.model.Address;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.repository.AddressRepository;
import com.mstftrgt.ebank.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    private final ModelMapper modelMapper;

    public CustomerService(CustomerRepository customerRepository, AddressRepository addressRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }


    public CustomerDto getCurrentCustomer() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Customer currentCustomer = (Customer) authentication.getPrincipal();

        Address address = addressRepository.findByCustomerId(currentCustomer.getId());

        AddressDto addressDto = modelMapper.map(address, AddressDto.class);

        CustomerDto customerDto = modelMapper.map(currentCustomer, CustomerDto.class);

        customerDto.setAddress(addressDto);

        return customerDto;
    }
}
