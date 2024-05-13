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

    @JoinColumn(name = "customer_id")
    private String customerId;

    private String accountNumber;

    private BigDecimal balance;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Account(String customerId, String accountNumber, BigDecimal balance) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
}
