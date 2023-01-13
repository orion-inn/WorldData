package com.javatraining.worlddata.repository;

import com.javatraining.worlddata.entity.Continent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContinentRepository extends JpaRepository<Continent, Integer> {
    Boolean existsByName(String name);
    Optional<Continent> findByName(String name);
}
