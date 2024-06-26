package com.mstftrgt.ebank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "districts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class District {

    @Id
    private Integer id;

    private String title;

    @JoinColumn(name = "city_id")
    private int cityId;
}
