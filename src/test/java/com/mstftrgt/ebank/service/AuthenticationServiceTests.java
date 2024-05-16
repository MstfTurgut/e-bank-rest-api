package com.mstftrgt.ebank.service;

import com.mstftrgt.ebank.dto.model.AddressDto;
import com.mstftrgt.ebank.dto.model.CityDto;
import com.mstftrgt.ebank.dto.model.CustomerDto;
import com.mstftrgt.ebank.dto.model.DistrictDto;
import com.mstftrgt.ebank.dto.request.auth.LoginCustomerRequest;
import com.mstftrgt.ebank.dto.request.auth.RegisterCustomerRequest;
import com.mstftrgt.ebank.exception.CityNotFoundException;
import com.mstftrgt.ebank.exception.DistrictNotFoundException;
import com.mstftrgt.ebank.exception.EmailAlreadyInUseException;
import com.mstftrgt.ebank.model.Address;
import com.mstftrgt.ebank.model.City;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.model.District;
import com.mstftrgt.ebank.repository.AddressRepository;
import com.mstftrgt.ebank.repository.CityRepository;
import com.mstftrgt.ebank.repository.CustomerRepository;
import com.mstftrgt.ebank.repository.DistrictRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private DistrictRepository districtRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private ModelMapper modelMapper;

    @Captor
    ArgumentCaptor<Address> addressCaptor;
    @Captor
    ArgumentCaptor<Customer> customerCaptor;
    @InjectMocks
    private AuthenticationService authenticationService;


    @Test
    void shouldRegisterNewCustomer_whenTheRequestIsValidAndEmailNotTakenAndGivenCityAndDistrictExists() {

        RegisterCustomerRequest registerRequest = new RegisterCustomerRequest("mock@gmail.com",
                "mock123", "John", "Doe", "123456789",
                "plain address", "Malatya", "Darende", LocalDateTime.now().minusYears(20));
        City city = new City(44, "Malatya");
        District district = new District(10, "Darende", 44);
        Customer savedCustomer = new Customer("customerId", "mock@gmail.com", "encodedPassword", "John",
                        "Doe", "123456789", LocalDateTime.now().minusYears(20));
        Address savedAddress = new Address("addressId", "plain address", city, district, "customerId");
        AddressDto addressDto = new AddressDto("addressId", "plain address",
                new CityDto(44, "Malatya"), new DistrictDto(10, "Darende"));
        CustomerDto customerDto = new CustomerDto("customerId", "mock@gmail.com", "John", "Doe", "123456789", LocalDateTime.now().minusYears(20), null);
        CustomerDto expectedResult = new CustomerDto("customerId", "mock@gmail.com", "John", "Doe", "123456789", LocalDateTime.now().minusYears(20), addressDto);


        Mockito.when(customerRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        Mockito.when(cityRepository.findByTitle(registerRequest.getCity())).thenReturn(Optional.of(city));
        Mockito.when(districtRepository.findByTitleAndCityId(registerRequest.getDistrict(), city.getId())).thenReturn(Optional.of(district));
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(savedCustomer);
        Mockito.when(addressRepository.save(Mockito.any(Address.class))).thenReturn(savedAddress);
        Mockito.when(modelMapper.map(savedCustomer, CustomerDto.class)).thenReturn(customerDto);
        Mockito.when(modelMapper.map(savedAddress, AddressDto.class)).thenReturn(addressDto);

        CustomerDto result = authenticationService.register(registerRequest);

        assertEquals(expectedResult, result);

        Mockito.verify(customerRepository).findByEmail(registerRequest.getEmail());
        Mockito.verify(cityRepository).findByTitle(registerRequest.getCity());
        Mockito.verify(districtRepository).findByTitleAndCityId(registerRequest.getDistrict(), city.getId());

        Mockito.verify(customerRepository).save(customerCaptor.capture());
        Customer capturedCustomer = customerCaptor.getValue();
        assertThat(capturedCustomer.getFirstName()).isEqualTo(registerRequest.getFirstName());
        assertThat(capturedCustomer.getLastName()).isEqualTo(registerRequest.getLastName());
        assertThat(capturedCustomer.getDateOfBirth()).isEqualTo(registerRequest.getDateOfBirth());
        assertThat(capturedCustomer.getEmail()).isEqualTo(registerRequest.getEmail());
        assertThat(capturedCustomer.getPassword()).isEqualTo(savedCustomer.getPassword());
        assertThat(capturedCustomer.getPhoneNumber()).isEqualTo(registerRequest.getPhoneNumber());

        Mockito.verify(addressRepository).save(addressCaptor.capture());
        Address capturedAddress = addressCaptor.getValue();
        assertThat(capturedAddress.getPlainAddress()).isEqualTo(registerRequest.getPlainAddress());
        assertThat(capturedAddress.getCity()).isEqualTo(city);
        assertThat(capturedAddress.getDistrict()).isEqualTo(district);
        assertThat(capturedAddress.getCustomerId()).isEqualTo(savedCustomer.getId());

        Mockito.verify(modelMapper).map(savedCustomer, CustomerDto.class);
        Mockito.verify(modelMapper).map(savedAddress, AddressDto.class);
    }

    @Test
    void shouldNotRegisterNewCustomer_whenTheRequestIsValidButEmailIsTaken() {
        RegisterCustomerRequest registerRequest = new RegisterCustomerRequest("mock@gmail.com",
                "mock123", "John", "Doe", "123456789",
                "plain address", "Malatya", "Darende", LocalDateTime.now().minusYears(20));
        Customer customer = new Customer("customerId", "mock@gmail.com", "encodedPassword", "John",
                "Doe", "123456789", LocalDateTime.now().minusYears(20));

        Mockito.when(customerRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> authenticationService.register(registerRequest))
                .isInstanceOf(EmailAlreadyInUseException.class)
                .hasMessageContaining("This email already in use.");


        Mockito.verifyNoInteractions(cityRepository);
        Mockito.verifyNoInteractions(districtRepository);
        Mockito.verifyNoInteractions(addressRepository);
    }

    @Test
    void shouldNotRegisterNewCustomer_whenTheRequestIsValidAndEmailIsNotTakenButGivenCityNotFound() {
        RegisterCustomerRequest registerRequest = new RegisterCustomerRequest("mock@gmail.com",
                "mock123", "John", "Doe", "123456789",
                "plain address", "Malatya", "Darende", LocalDateTime.now().minusYears(20));

        Mockito.when(customerRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        Mockito.when(cityRepository.findByTitle(registerRequest.getCity())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.register(registerRequest))
                .isInstanceOf(CityNotFoundException.class)
                .hasMessageContaining("City not found.");

        Mockito.verifyNoInteractions(districtRepository);
        Mockito.verifyNoInteractions(addressRepository);
    }


    @Test
    void shouldNotRegisterNewCustomer_whenTheRequestIsValidAndEmailIsNotTakenAndGivenCityExistsButDistrictNotFound() {

        RegisterCustomerRequest registerRequest = new RegisterCustomerRequest("mock@gmail.com",
                "mock123", "John", "Doe", "123456789",
                "plain address", "Malatya", "Darende", LocalDateTime.now().minusYears(20));
        City city = new City(44, "Malatya");

        Mockito.when(customerRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        Mockito.when(cityRepository.findByTitle(registerRequest.getCity())).thenReturn(Optional.of(city));
        Mockito.when(districtRepository.findByTitleAndCityId(registerRequest.getDistrict(), city.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.register(registerRequest))
                .isInstanceOf(DistrictNotFoundException.class)
                .hasMessageContaining("District " + registerRequest.getDistrict() +  " not found for the city : " + city.getTitle());

        Mockito.verifyNoInteractions(addressRepository);

    }

    @Test
    void shouldLoginCustomer_whenTheCustomerFoundForTheCredentials() {

        LoginCustomerRequest loginRequest = new LoginCustomerRequest("mock@gmail.com", "mock123");
        Customer expectedResult = new Customer("customerId", "mock@gmail.com", "mock123", "John",
                "Doe", "123456789", LocalDateTime.now().minusYears(20));

        Mockito.when(customerRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(expectedResult));

        Customer result = authenticationService.login(loginRequest);

        assertEquals(expectedResult, result);

        Mockito.verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        Mockito.verify(customerRepository).findByEmail(loginRequest.getEmail());
    }

    @Test
    void shouldNotLoginCustomer_whenTheCustomerNotFoundForTheCredentials() {

        LoginCustomerRequest loginRequest = new LoginCustomerRequest("mock@gmail.com", "mock123");

        Mockito.when(customerRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.login(loginRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No value present");

        Mockito.verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        Mockito.verify(customerRepository).findByEmail(loginRequest.getEmail());
    }


}
