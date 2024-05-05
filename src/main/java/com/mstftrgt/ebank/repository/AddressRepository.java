package com.mstftrgt.ebank.repository;

import com.mstftrgt.ebank.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    Address findByCustomerId(String customerId);

}
