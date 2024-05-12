package com.mstftrgt.ebank.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class AddressDto {
    private String id;
    private String plainAddress;
    private CityDto city;
    private DistrictDto district;
}
