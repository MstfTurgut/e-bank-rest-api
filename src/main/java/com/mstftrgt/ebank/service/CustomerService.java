package com.mstftrgt.ebank.service;

import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.dto.model.CustomerDto;
import com.mstftrgt.ebank.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerService(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }


    public List<CustomerDto> allCustomers() {

        List<Customer> customers = customerRepository.findAll();

        return customers.stream().map(customer -> modelMapper.map(customer, CustomerDto.class
        )).toList();
    }
}

