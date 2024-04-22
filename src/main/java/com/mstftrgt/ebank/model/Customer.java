package com.mstftrgt.ebank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "customers")
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String email;

    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Set<Account> accounts;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_details_id")
    private CustomerDetails customerDetails;
    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public void addAccount(Account account) {
        if(accounts == null) accounts = new HashSet<>();
        accounts.add(account);
    }

}
