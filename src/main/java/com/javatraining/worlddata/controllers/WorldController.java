package com.javatraining.worlddata.controllers;

import com.javatraining.worlddata.dao.WorldDao;
import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.entity.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/world")
public class WorldController {

    @Autowired
    private WorldDao worldDao;

    /* Cities */

    @GetMapping("/cities/{id}")
    public City getCityById(@PathVariable int id) {
        log.info("Get city with ID=" + id);
        return worldDao.findCityById(id);
    }

    @GetMapping("/cities/ids")
    public List<City> getCitiesWithIds(@RequestBody List<Integer> ids) {
        log.info("Get cities with IDs=" + ids);
        return worldDao.findCitiesWithIds(ids);
    }

    @GetMapping("/cities/name/{name}")
    public List<City> getCitiesByPartialName(@PathVariable String name) {
        log.info("Get cities with name containing '" + name + "'");
        return worldDao.findCitiesByPartialName(name);
    }

    @GetMapping("/cities")
    public List<City> getAllCities() {
        log.info("Get all cities");
        return worldDao.findAllCities();
    }

    @PostMapping("/cities")
    public void addCity(@RequestBody City city) {
        log.info("Add new city: " + city);
        worldDao.addCity(city);
    }

    @PutMapping("/cities")
    public void updateCity(@RequestBody City city) {
        log.info("Update city: " + city);
        worldDao.updateCity(city);
    }

    @DeleteMapping("/cities")
    public void deleteCity(@RequestBody City city) {
        log.info("Remove city: " + city);
        worldDao.removeCity(worldDao.findCityById(city.getId()));
    }

    /* Countries */

    @GetMapping("/countries/{code}")
    public Country getCountryByCode(@PathVariable String code) {
        log.info("Get country with code=" + code);
        return worldDao.findCountryByCode(code);
    }

    @GetMapping("/countries/codes")
    public List<Country> getCountriesByCodes(@RequestBody List<String> codes) {
        log.info("Get countries with codes=" + codes);
        return worldDao.findCountriesByCodes(codes);
    }

    @GetMapping("/countries/name/{name}")
    public List<Country> getCountriesByPartialName(@PathVariable String name) {
        log.info("Get countries with name containing '" + name + "'");
        return worldDao.findCountriesByPartialName(name);
    }

    @GetMapping("/countries")
    public List<Country> getAllCountries() {
        log.info("Get all countries");
        return worldDao.findAllCountries();
    }

    @PostMapping("/countries")
    public void addCountry(@RequestBody Country country) {
        log.info("Add new country: " + country );
        worldDao.addCountry(country);
    }

    @PutMapping("/countries")
    public void updateCountry(@RequestBody Country country) {
        log.info("Update country: " + country);
        worldDao.updateCountry(country);
    }

    @DeleteMapping("/countries")
    public void deleteCountry(@RequestBody Country country) {
        log.info("Remove country: " + country);
        worldDao.removeCountry(worldDao.findCountryByCode(country.getCode()));
    }
}
