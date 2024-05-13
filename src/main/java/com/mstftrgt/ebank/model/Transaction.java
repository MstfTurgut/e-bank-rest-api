package com.mstftrgt.ebank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "sender_account_id")
    private Account senderAccount;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "receiver_account_id")
    private Account receiverAccount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private BigDecimal amount;

    @CreationTimestamp
    private LocalDateTime date;

    private String description;

    public Transaction(Account senderAccount, Account receiverAccount, TransactionType transactionType, BigDecimal amount, String description) {
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
    }

    public enum TransactionType{INITIAL, TRANSFER}
}
