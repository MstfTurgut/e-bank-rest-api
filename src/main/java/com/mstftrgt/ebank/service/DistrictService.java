package com.mstftrgt.ebank.service;

import com.mstftrgt.ebank.dto.model.DistrictDto;
import com.mstftrgt.ebank.repository.DistrictRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistrictService {

    private final DistrictRepository districtRepository;

    private final ModelMapper modelMapper;

    public DistrictService(DistrictRepository districtRepository, ModelMapper modelMapper) {
        this.districtRepository = districtRepository;
        this.modelMapper = modelMapper;
    }

    public List<DistrictDto> getAllDistrictsByCityId(int cityId) {
        return districtRepository
                .findByCityId(cityId)
                .stream()
                .map(district -> modelMapper.map(district, DistrictDto.class))
                .collect(Collectors.toList());
    }
}
