package com.javatraining.worlddata.service;

import com.javatraining.worlddata.entity.Continent;
import com.javatraining.worlddata.entity.Country;
import com.javatraining.worlddata.exception.ResourceAlreadyExistsException;
import com.javatraining.worlddata.exception.ResourceNotFoundException;
import com.javatraining.worlddata.repository.ContinentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ContinentService {

    @Autowired
    private ContinentRepository repository;

    public Continent create(Continent continent) {
        log.debug("Creating continent");
        if (repository.existsByName(continent.getName())) {
            throw new ResourceAlreadyExistsException("Continent with name='" + continent.getName() + "' already exists");
        }

        return repository.save(continent);
    }

    public Continent update(int id, Continent continent) {
        log.debug("Updating continent");
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Continent with id=" + id + " not found");
        }

        continent.setId(id);

        return repository.save(continent);
    }

    public void delete(int id) {
        log.debug("Deleting continent");
        Continent continentToDelete = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Continent with id=" + id + " not found"));

        for (Country country : continentToDelete.getCountries()) {
            country.setContinent(null);
        }

        repository.delete(continentToDelete);
    }

    public Continent getById(int id) {
        log.debug("Getting continent by ID");
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Continent with id=" + id + " not found"));
    }

    public Continent getByName(String name) {
        log.debug("Getting continent by name");
        return repository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Continent with name='" + name + "' not found"));
    }

    public List<Continent> getAll() {
        log.debug("Getting all continents");
        return repository.findAll();
    }
}
