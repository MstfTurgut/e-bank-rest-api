package com.mstftrgt.ebank.repository;

import com.mstftrgt.ebank.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    Page<Transaction> findBySenderAccountIdOrReceiverAccountId(String senderAccountId, String receiverAccountId,PageRequest pageRequest);

}
