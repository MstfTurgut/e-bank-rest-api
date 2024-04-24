package com.mstftrgt.ebank.repository;

import com.mstftrgt.ebank.model.Account;
import com.mstftrgt.ebank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    List<Account> findAllByCustomerId(String customerId);
    default List<Account> findAll() {
        throw new UnsupportedOperationException("unsupported, please use findAllByCustomerId instead");
    }

}
