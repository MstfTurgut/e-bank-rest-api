package com.mstftrgt.ebank.repository;

import com.mstftrgt.ebank.model.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Integer> {


    List<District> findByCityId(int cityId);

    Optional<District> findByTitleAndCityId(String title, int cityId);

}
