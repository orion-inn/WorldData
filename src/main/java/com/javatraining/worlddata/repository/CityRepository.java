package com.javatraining.worlddata.repository;

import com.javatraining.worlddata.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByName(String name);

    List<City> findAllByCountryName(String country);
}
