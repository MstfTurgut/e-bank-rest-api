package com.mstftrgt.ebank.repository;

import com.mstftrgt.ebank.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    Page<Transaction> findBySenderAccountIdOrReceiverAccountId(String senderAccountId, String receiverAccountId,PageRequest pageRequest);

    @Query(nativeQuery = true,
            value = "SELECT FROM transactions t WHERE t.sender_account_id = ?1 AND t.transaction_type = 'INITIAL'")
    Optional<Transaction> findInitialTransactionByAccountId(String accountId);

}
