
package com.mstftrgt.ebank.service;

import com.mstftrgt.ebank.dto.model.AddressDto;
import com.mstftrgt.ebank.dto.model.CityDto;
import com.mstftrgt.ebank.dto.model.CustomerDto;
import com.mstftrgt.ebank.dto.model.DistrictDto;
import com.mstftrgt.ebank.model.Address;
import com.mstftrgt.ebank.model.City;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.model.District;
import com.mstftrgt.ebank.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

public class CustomerServiceTests {

    private CustomerService customerService;
    private AddressRepository addressRepository;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        addressRepository = Mockito.mock(AddressRepository.class);
        modelMapper = Mockito.mock(ModelMapper.class);

        customerService = new CustomerService(addressRepository, modelMapper);
    }


    @DisplayName("should Return Current CustomerDto When A Customer Logged In")
    @Test
    void shouldReturnCurrentCustomerDtoWhenACustomerLoggedIn() {

        Customer customer =
                new Customer("customerId", "email@email.com", "password",
                        "ali", "veli", "123456789", LocalDateTime.now());

        District district = new District(10, "Esenyurt", 34);
        City city = new City(34, "Istanbul");
        Address address = new Address("addressId", "plain address", city, district, "customerId");

        CityDto cityDto = new CityDto(34, "Istanbul");
        DistrictDto districtDto = new DistrictDto(10, "Esenyurt");
        AddressDto addressDto = new AddressDto("addressId", "plain address", cityDto, districtDto);
        CustomerDto customerDtoWithoutAddress = new CustomerDto("customerId", "email@email.com",
                "ali", "veli", "123456789", LocalDateTime.now(), null);
        CustomerDto expectedResult = new CustomerDto("customerId", "email@email.com",
                "ali", "veli", "123456789", LocalDateTime.now(), addressDto);


        Mockito.when(addressRepository.findByCustomerId(customer.getId())).thenReturn(address);
        Mockito.when(modelMapper.map(address, AddressDto.class)).thenReturn(addressDto);
        Mockito.when(modelMapper.map(customer, CustomerDto.class)).thenReturn(customerDtoWithoutAddress);

        CustomerDto result = customerService.getCurrentCustomer(customer);

        assertEquals(expectedResult, result);

        Mockito.verify(addressRepository).findByCustomerId(customer.getId());
        Mockito.verify(modelMapper).map(address, AddressDto.class);
        Mockito.verify(modelMapper).map(customer, CustomerDto.class);

    }

}

