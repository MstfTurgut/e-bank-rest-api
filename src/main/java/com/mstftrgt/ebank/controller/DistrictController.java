package com.mstftrgt.ebank.controller;

import com.mstftrgt.ebank.dto.model.DistrictDto;
import com.mstftrgt.ebank.service.DistrictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/districts")
public class DistrictController {

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<List<DistrictDto>> getAllDistrictsByCityId(@PathVariable int cityId) {
        return ResponseEntity.ok(districtService.getAllDistrictsByCityId(cityId));
    }

}
