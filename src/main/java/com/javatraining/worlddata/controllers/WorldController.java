package com.javatraining.worlddata.controllers;

import com.javatraining.worlddata.dao.WorldDao;
import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.entity.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/world")
public class WorldController {

    @Autowired
    private WorldDao worldDao;

    /* Cities */

    @GetMapping("/cities/{id}")
    @ResponseStatus(HttpStatus.OK)
    public City getCityById(@PathVariable int id) {
        log.info("Get city with ID " + id);
        City city = worldDao.findCityById(id);
        if (city == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City with ID " + id + " not found");
        }
        return city;
    }

    @GetMapping("/cities/ids")
    @ResponseStatus(HttpStatus.OK)
    public List<City> getCitiesWithIds(@RequestBody List<Integer> ids) {
        log.info("Get cities with IDs " + ids);
        List<City> cities = ids.stream()
                .map(worldDao::findCityById)
                .filter(Objects::nonNull)
                .toList();
        if (cities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cities with these IDs " + ids);
        }
        return cities;
    }

    @GetMapping("/cities/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<City> getCitiesByPartialName(@PathVariable String name) {
        log.info("Get cities with name containing '" + name + "'");
        List<City> cities = worldDao.findCitiesByPartialName(name);
        if (cities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cities with name containing '" + name + "'");
        }
        return cities;
    }

    @GetMapping("/cities")
    @ResponseStatus(HttpStatus.OK)
    public List<City> getAllCities() {
        log.info("Get all cities");
        List<City> cities = worldDao.findAllCities();
        if (cities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cities found in the DB");
        }
        return cities;
    }

    @PostMapping("/cities")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCity(@RequestBody City city) {
        log.info("Add new city: " + city);
        try {
            worldDao.addCity(city);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not add the city", e);
        }
    }

    @PutMapping("/cities")
    @ResponseStatus(HttpStatus.OK)
    public void updateCity(@RequestBody City city) {
        log.info("Update city: " + city);
        try {
            worldDao.updateCity(city);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not update the city", e);
        }
    }

    @DeleteMapping("/cities")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCity(@RequestBody City city) {
        log.info("Remove city: " + city);
        City cityToDelete = worldDao.findCityById(city.getId());
        if (cityToDelete == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not delete the city. City not found");
        }
        try {
            worldDao.removeCity(cityToDelete);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not delete the city", e);
        }
    }

    /* Countries */

    @GetMapping("/countries/{code}")
    @ResponseStatus(HttpStatus.OK)
    public Country getCountryByCode(@PathVariable String code) {
        log.info("Get country with code " + code);
        Country country = worldDao.findCountryByCode(code);
        if (country == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Country with code " + code + " not found");
        }
        return country;
    }

    @GetMapping("/countries/codes")
    @ResponseStatus(HttpStatus.OK)
    public List<Country> getCountriesByCodes(@RequestBody List<String> codes) {
        log.info("Get countries with codes " + codes);
        List<Country> countries = codes.stream()
                .map(worldDao::findCountryByCode)
                .filter(Objects::nonNull)
                .toList();
        if (countries.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No countries with these codes " + codes);
        }
        return countries;
    }

    @GetMapping("/countries/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<Country> getCountriesByPartialName(@PathVariable String name) {
        log.info("Get countries with name containing '" + name + "'");
        List<Country> countries = worldDao.findCountriesByPartialName(name);
        if (countries.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No countries with name containing '" + name + "'");
        }
        return countries;
    }

    @GetMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    public List<Country> getAllCountries() {
        log.info("Get all countries");
        List<Country> countries = worldDao.findAllCountries();
        if (countries.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No countries found in the DB");
        }
        return countries;
    }

    @PostMapping("/countries")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCountry(@RequestBody Country country) {
        log.info("Add new country: " + country);
        try {
            worldDao.addCountry(country);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not add the country", e);
        }
    }

    @PutMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    public void updateCountry(@RequestBody Country country) {
        log.info("Update country: " + country);
        try {
            worldDao.updateCountry(country);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not update the country", e);
        }
    }

    @DeleteMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCountry(@RequestBody Country country) {
        log.info("Remove country: " + country);
        Country countryToDelete = worldDao.findCountryByCode(country.getCode());
        if (countryToDelete == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not delete the country. Country not found");
        }
        try {
            worldDao.removeCountry(countryToDelete);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not delete the country", e);
        }
    }
}
