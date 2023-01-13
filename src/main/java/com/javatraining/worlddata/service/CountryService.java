package com.javatraining.worlddata.service;

import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.entity.Country;
import com.javatraining.worlddata.exception.ResourceAlreadyExistsException;
import com.javatraining.worlddata.exception.ResourceNotFoundException;
import com.javatraining.worlddata.repository.CountryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CountryService {

    @Autowired
    private CountryRepository repository;

    public Country create(Country country) {
        log.debug("Creating country");
        if(repository.existsByName(country.getName())) {
            throw new ResourceAlreadyExistsException("Country with name='" + country.getName() + "' already exists");
        }

        return repository.save(country);
    }

    public Country update(int id, Country country) {
        log.debug("Updating country");
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Country with id=" + id + " not found");
        }

        country.setId(id);

        return repository.save(country);
    }

    public void delete(int id) {
        log.debug("Deleting country");
        Country countryToDelete = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country with id=" + id + " not found"));

        for (City city : countryToDelete.getCities()) {
            city.setCountry(null);
        }

        repository.delete(countryToDelete);
    }

    public Country getById(int id) {
        log.debug("Getting country by ID");
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country with id=" + id + " not found"));
    }

    public Country getByName(String name) {
        log.debug("Getting country by name");
        return repository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country with name='" + name + "' not found"));
    }

    public List<Country> getAll() {
        log.debug("Getting all countries");
        return repository.findAll();
    }

    public List<Country> getAllByContinentName(String continent) {
        log.debug("Getting all countries by continent name");
        return repository.findAllByContinentName(continent);
    }
}
