package com.mstftrgt.ebank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String plainAddress;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    @JoinColumn(name = "customer_id")
    private String customerId;

    public Address(String plainAddress, City city, District district, String customerId) {
        this.plainAddress = plainAddress;
        this.city = city;
        this.district = district;
        this.customerId = customerId;
    }
}
