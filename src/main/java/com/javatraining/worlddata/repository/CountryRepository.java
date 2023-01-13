package com.javatraining.worlddata.repository;

import com.javatraining.worlddata.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    Boolean existsByName(String name);
    Optional<Country> findByName(String name);
    List<Country> findAllByContinentName(String continent);
}
