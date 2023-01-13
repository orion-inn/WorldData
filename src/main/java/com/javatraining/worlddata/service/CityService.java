package com.javatraining.worlddata.service;

import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.exception.ResourceAlreadyExistsException;
import com.javatraining.worlddata.exception.ResourceNotFoundException;
import com.javatraining.worlddata.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CityService {

    @Autowired
    private CityRepository repository;

    public City create(City city) {
        log.debug("Creating city");
        if (repository.existsByName(city.getName())) {
            throw new ResourceAlreadyExistsException("City with name='" + city.getName() + "' already exists");
        }

        return repository.save(city);
    }

    public City update(int id, City city) {
        log.debug("Updating city");
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("City with id=" + id + " not found");
        }

        city.setId(id);

        return repository.save(city);
    }

    public void delete(int id) {
        log.debug("Deleting city");
        City cityToDelete = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("City with id=" + id + " not found"));

        repository.delete(cityToDelete);
    }

    public City getById(int id) {
        log.debug("Getting city by ID");
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("City with id=" + id + " not found"));
    }

    public City getByName(String name) {
        log.debug("Getting city by name");
        return repository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("City with name='" + name + "' not found"));
    }

    public List<City> getAll() {
        log.debug("Getting all cities");
        return repository.findAll();
    }

    public List<City> getAllByCountryName(String country) {
        log.debug("Getting all cities by country name");
        return repository.findAllByCountryName(country);
    }
}
