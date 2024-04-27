package com.mstftrgt.ebank.repository;

import com.mstftrgt.ebank.model.Account;
import com.mstftrgt.ebank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    List<Account> findAllByCustomerId(String customerId);
    default List<Account> findAll() {
        throw new UnsupportedOperationException("unsupported, please use findAllByCustomerId instead");
    }

    Optional<Account> findAccountByAccountNumber(String accountNumber);

}
