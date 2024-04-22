package com.mstftrgt.ebank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private BigDecimal amount;

    private LocalDateTime date;

    private String description;
    public Transaction(TransactionType transactionType, BigDecimal amount, LocalDateTime date, String description) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public enum TransactionType{INITIAL, TRANSFER}
}
