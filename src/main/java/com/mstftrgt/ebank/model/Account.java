package com.mstftrgt.ebank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String accountNumber;

    private BigDecimal balance;

    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "account")
    private Set<Card> cards;

    @OneToMany(mappedBy = "account")
    private Set<Transaction> transactions;

    public Account(String accountNumber, BigDecimal balance, LocalDateTime creationDate) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.creationDate = creationDate;
    }

    public void addCard(Card card) {
        if (cards == null) cards = new HashSet<>();
        cards.add(card);
    }

    public void addTransaction(Transaction transaction) {
        if (transactions == null) transactions = new HashSet<>();
        transactions.add(transaction);
    }
}
