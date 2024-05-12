package com.mstftrgt.ebank.service;

import com.mstftrgt.ebank.dto.model.AddressDto;
import com.mstftrgt.ebank.dto.model.CustomerDto;
import com.mstftrgt.ebank.model.Address;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.repository.AddressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final AddressRepository addressRepository;

    private final ModelMapper modelMapper;

    public CustomerService(AddressRepository addressRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }


    public CustomerDto getCurrentCustomer(Customer customer) {

        Address address = addressRepository.findByCustomerId(customer.getId());

        AddressDto addressDto = modelMapper.map(address, AddressDto.class);

        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);

        customerDto.setAddress(addressDto);

        return customerDto;
    }
}
