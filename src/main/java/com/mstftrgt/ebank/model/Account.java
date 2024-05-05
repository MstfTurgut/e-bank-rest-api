package com.mstftrgt.ebank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String accountNumber;

    private BigDecimal balance;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Account(String accountNumber, BigDecimal balance, LocalDateTime createdAt) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.createdAt = createdAt;
    }
}
