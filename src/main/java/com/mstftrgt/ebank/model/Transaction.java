package com.mstftrgt.ebank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "sender_account_id")
    private Account senderAccount;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "receiver_account_id")
    private Account receiverAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private BigDecimal amount;

    @CreationTimestamp
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
