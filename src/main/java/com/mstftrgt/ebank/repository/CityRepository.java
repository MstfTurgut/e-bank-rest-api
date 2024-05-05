package com.mstftrgt.ebank.repository;

import com.mstftrgt.ebank.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByTitle(String title);
}
