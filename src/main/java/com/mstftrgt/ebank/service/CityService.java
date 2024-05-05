package com.mstftrgt.ebank.service;

import com.mstftrgt.ebank.dto.model.CityDto;
import com.mstftrgt.ebank.repository.CityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final CityRepository cityRepository;

    private final ModelMapper modelMapper;

    public CityService(CityRepository cityRepository, ModelMapper modelMapper) {
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
    }

    public List<CityDto> getAllCities() {

        return cityRepository
                .findAll()
                .stream()
                .map(city -> modelMapper.map(city, CityDto.class))
                .collect(Collectors.toList());
    }
}
