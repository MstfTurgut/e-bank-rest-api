package com.mstftrgt.ebank.dto.model;

import lombok.Data;
@Data
public class AddressDto {
    private String id;
    private String plainAddress;
    private CityDto city;
    private DistrictDto district;
}
