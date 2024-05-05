package com.mstftrgt.ebank.controller;

import com.mstftrgt.ebank.dto.model.CityDto;
import com.mstftrgt.ebank.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<CityDto>> getAllCities() {

        return ResponseEntity.ok(cityService.getAllCities());

    }

}
